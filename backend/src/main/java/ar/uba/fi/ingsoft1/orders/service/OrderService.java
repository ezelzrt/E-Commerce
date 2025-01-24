package ar.uba.fi.ingsoft1.orders.service;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Optional;
import java.time.LocalDateTime;

import ar.uba.fi.ingsoft1.exception.InsuficientStockException;
import ar.uba.fi.ingsoft1.exception.InvalidStatusChangeException;
import ar.uba.fi.ingsoft1.products.repository.Product;
import ar.uba.fi.ingsoft1.products.repository.ProductRepository;
import ar.uba.fi.ingsoft1.products.service.ProductService;
import ar.uba.fi.ingsoft1.orders.repository.Order;
import ar.uba.fi.ingsoft1.orders.repository.OrderDetail;
import ar.uba.fi.ingsoft1.orders.repository.OrderDetailRepository;
import ar.uba.fi.ingsoft1.orders.repository.OrderStatus;
import ar.uba.fi.ingsoft1.orders.repository.OrderRepository;
import ar.uba.fi.ingsoft1.orders.controller.ProductOrder;
import ar.uba.fi.ingsoft1.rules.service.RuleService;
import ar.uba.fi.ingsoft1.user.User;
import ar.uba.fi.ingsoft1.user.UserService;
import lombok.RequiredArgsConstructor;



import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private RuleService ruleService;
        
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }
    
    public List<Order> getOrdersByUserId(Long user_id) {
        return orderRepository.findByUser_Id(user_id);
    }
    
    public List<OrderDetail> getOrderDetailsByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

    public OrderStatus patchStatusById(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId).get();
        OrderStatus orderStatus = order.getStatus();
        
        if (orderStatus.isCancelledStatus()){
            throw new InvalidStatusChangeException("The order is canceled");
        }
        order = newStatus.manageOrder(this, order);

        return orderRepository.save(order).getStatus();
    }
    
    public OrderStatus userPatchStatusById(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId).get();
        OrderStatus orderStatus = order.getStatus();
        
        if (orderStatus.isCancelledStatus()){
            throw new InvalidStatusChangeException("The order is canceled");
        }
        if (!orderStatus.isUserModificable()){
            throw new InvalidStatusChangeException("Your user don't have access to this resource");
        }
        if (!orderStatus.nextStatus().isUserModificable() && !newStatus.isCancelledStatus()){
            throw new InvalidStatusChangeException("Invalid New Status: " + newStatus.toString());
        }

        LocalDateTime limitTime = order.getConfirmationDate().plusHours(24);
        if (orderStatus == OrderStatus.CONFIRMED && LocalDateTime.now().isAfter(limitTime)){
            throw new InvalidStatusChangeException("After 24 hours of confirming the order, it is not possible to cancel it");
        }
        order = newStatus.manageOrder(this, order);

        return orderRepository.save(order).getStatus();
    }

    
    public Order confirmOrder(Order order){
        order.setConfirmationDate(LocalDateTime.now());
        order.setStatus(OrderStatus.CONFIRMED);
        return order;
    }
    
    public Order progressOrder(Order order){
        order.setStatus(OrderStatus.IN_PROGRESS);
        return order;
    }
    
    public Order deliverOrder(Order order){
        // Se remueve definitivamente el compromised stock 
        for (OrderDetail item: order.getOrderDetails()){
            Long productId = item.getProduct().getId();
            Long amount = item.getProductAmount();
            
            productService.updateCompromisedStockProductDelivered(productId, amount);
        }
        order.setStatus(OrderStatus.DELIVERED);
        return order;
    }

    public Order cancelOrder(Order order){
        OrderStatus orderStatus = order.getStatus();
   
        for (OrderDetail item: order.getOrderDetails()){
                Long productId = item.getProduct().getId();
                Long amount = item.getProductAmount();
            if (orderStatus == OrderStatus.DELIVERED){
                // Se devuelve al stock desde los OrderDetail
                productService.patchStockProduct(productId, amount);
            } else {
                // Se devuelve al stock desde el compromisedStock
                productService.patchCompromisedStockProduct(productId, -amount);
            }
        }
        order.setStatus(OrderStatus.CANCELLED);
        return order;
    }


    // Flujo para crear orden
    // 1. Por cada producto de la orden validar que el id del producto exista y que dispongamos del stock. Devolver error ante fallo
    // 2. Aplicar reglas a pedido (super TODO). Las reglas se aplican secuencialmente y de fallar alguna debe proponer modificacion al pedido
    // 3. Actualizar la base de datos de productos, pasando el stock de disponible a comprometido y agregar orden a base de ordenes
    @Transactional
    public List<Order> createOrder(Long userId, List<ProductOrder> items) {

        verifyOrder(items);    // Validar item y stock suficiente

        Long initialOrderId = 1L;
        List<OrderDetail> initialsOrderDetails = createInitialsOrderdetails(items);

        HashMap<Long, List<OrderDetail>> initialOrders = new HashMap<>();
        initialOrders.put(initialOrderId, initialsOrderDetails);
        
        Long actOrderId = initialOrderId;
        while (true) {
            Long nextOrderId = actOrderId + 1L;
            initialOrders.put(nextOrderId, new ArrayList<>());
            HashMap<Long, List<OrderDetail>> newGhostOrders = ruleService.apply(actOrderId, initialOrders)
                .orElseThrow();

            if (newGhostOrders.get(actOrderId).isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The current order is invalid");
            }
            if (newGhostOrders.get(nextOrderId).isEmpty()) {
                newGhostOrders.remove(nextOrderId);
                break;
            }
            actOrderId += 1L;
        }

        List<Order> finalOrders = new ArrayList<>();
        User user = userService.getUserById(userId);
        for ( Long orderId: initialOrders.keySet() ){
            Order savedOrder = orderRepository.save(createFinalOrder(user));
            finalOrders.add(savedOrder);
            for (OrderDetail orderDetail: initialOrders.get(orderId) ){
                OrderDetail finalOrderDetail = createFinalOrderDetail(savedOrder, orderDetail);
                orderDetailRepository.save(finalOrderDetail);
            }       
        }
        updateStock(items);
        return finalOrders;
    }
    
    /*
    * Verifica existencia del producto y stock suficiente.
    */
    private void verifyOrder(List<ProductOrder> items) {
        for (ProductOrder itemRequest : items) {
            Product product = productService.getProductById(itemRequest.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: " + itemRequest.getProductId()));
            
            if (product.getStock() < itemRequest.getAmount()) {
                throw new InsuficientStockException("Insuficient stock for product: [" + itemRequest.getProductId() + "] " + productRepository.findById(itemRequest.getProductId()).get().getName());
            }
        }
    }
    
    
    /*
    * Modifica por cada item/producto el stock necesario a stock comprometido.
    * En caso de no haber stock suficiente o no existir el producto, lanza la excepción correspondiente.
    */
    private void updateStock(List<ProductOrder> items) {
        for (ProductOrder itemRequest : items) {
            productService.patchCompromisedStockProduct(itemRequest.getProductId(), itemRequest.getAmount())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        }
    }
    
    private List<OrderDetail> createInitialsOrderdetails(List<ProductOrder> items) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (ProductOrder itemRequest : items) {
            orderDetails.add(createInitialOrderDetail(itemRequest));
        }
        return orderDetails;
    }
    
    private OrderDetail createInitialOrderDetail(ProductOrder itemRequest) {
        Long productId = itemRequest.getProductId();
        Product product = productService.getProductById(productId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return OrderDetail.builder()
                .product(product)
                .productAmount(itemRequest.getAmount())
                .productPrice(product.getPrice())
                .build();
    }
    
    private Order createFinalOrder(User user) {
        return Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .build();
    }
    
    private OrderDetail createFinalOrderDetail(Order order, OrderDetail oldOrderDetail) {
        return OrderDetail.builder()
                .order(order)
                .product(oldOrderDetail.getProduct())
                .productAmount(oldOrderDetail.getProductAmount())
                .productPrice(oldOrderDetail.getProductPrice())
                .build();
    }

}
