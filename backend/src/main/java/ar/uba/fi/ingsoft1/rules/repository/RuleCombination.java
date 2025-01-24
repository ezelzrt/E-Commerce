package ar.uba.fi.ingsoft1.rules.repository;

import java.util.HashMap;
import java.util.List;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import ar.uba.fi.ingsoft1.orders.repository.Order;
import ar.uba.fi.ingsoft1.orders.repository.OrderDetail;
import ar.uba.fi.ingsoft1.orders.service.OrderService;
import ar.uba.fi.ingsoft1.products.repository.ProductRepository;
import ar.uba.fi.ingsoft1.products.repository.Product;
import ar.uba.fi.ingsoft1.attributes.repository.AttributeValue;
import ar.uba.fi.ingsoft1.products.service.ProductService;

public class RuleCombination implements Rule {
    
    // private final OrderService orderService;
    
    private Long value;
    private Long followerId;

    public RuleCombination(AbstracRule abstracRule) {
        // aca debería obtener todo lo necesario que tenga la regla abstracta
        this.value = abstracRule.getValue();                // Elemento 1
        this.followerId = abstracRule.getRulesFollowerId(); // Elemento 2
    }

    @Override
    public HashMap<Long, List<OrderDetail>> applyWithMasterPoduct(Long actOrderId,
            HashMap<Long, List<OrderDetail>> orders) {
        // TODO (innecesario por consigna)
        orders.put(actOrderId +1L,new ArrayList<>());
        return orders;
    }

    @Override
    public HashMap<Long, List<OrderDetail>> applyWithProduct(Long actOrderId,
            HashMap<Long, List<OrderDetail>> orders) {

        // TODO (innecesario por consigna)
        orders.put(actOrderId +1L,new ArrayList<>());
        return orders;
    }

    @Override
    public HashMap<Long, List<OrderDetail>> applyWithAttribute(Long actOrderId,
            HashMap<Long, List<OrderDetail>> orders) {
        Long nextOrderId = actOrderId + 1L;
	    List<OrderDetail> actOrderDetails = orders.get(actOrderId);
        List<OrderDetail> nextOrderDetails = new ArrayList<>();
        
        boolean contains_first  = false;
        boolean contains_second = false;
        boolean actual_second;
        
        Product product;
        int i = 0;
        for (OrderDetail orderDetail: actOrderDetails) {
            actual_second = false;
            product = orderDetail.getProduct();
            if ( product.containsAttribute(this.value) ){
                contains_first = true;
            } else if ( product.containsAttribute(this.followerId)){
                contains_second = true;
                actual_second = true;
            }            

            // Si debemos separar la orden ya que contiene de ambos atributos, e
            if (contains_first && contains_second && actual_second) {
                actOrderDetails.remove(i);
                nextOrderDetails.add(orderDetail);
            }

            i += 1;
        }

        orders.put(nextOrderId, nextOrderDetails);
        return orders;
    }

    @Override
    public HashMap<Long, List<OrderDetail>> applyWithAttributeValue(Long actOrderId,
            HashMap<Long, List<OrderDetail>> orders) {
        Long nextOrderId = actOrderId + 1L;
	    List<OrderDetail> actOrderDetails = orders.get(actOrderId);
        List<OrderDetail> nextOrderDetails = new ArrayList<>();
        
        boolean contains_first  = false;
        boolean contains_second = false;
        List<Integer> second_position = new ArrayList<>();
        Product product;
        List<AttributeValue> attributes;
        int i = 0;
        for (OrderDetail orderDetail: actOrderDetails) {
            product = orderDetail.getProduct();
            attributes = product.getAttributeValues();
            if ( this.containsAttribute(this.value, attributes) ){
                contains_first = true;
            } else if ( this.containsAttribute(this.followerId, attributes)){
                contains_second = true;
                second_position.add(i);
            }            

            i += 1;
        }

        // Separamos todo lo que sea el segundo atributo
        if (contains_first && contains_second ) {
            for (int ii=0; ii < second_position.size(); ii++){
                nextOrderDetails.add(actOrderDetails.remove(ii));
            }
        }

        orders.put(nextOrderId, nextOrderDetails);
        return orders;
    }

    private boolean containsAttribute(Long id, List<AttributeValue> attributes) {
        for (AttributeValue value: attributes){
            if (value.getId() == id){
                return true;
            }
        }
        return false;
    }

    // @Override
    // public HashMap<Order, List<OrderDetail>> apply(HashMap<Order, List<OrderDetail>> orders) {
    //     HashMap<Order, List<OrderDetail>> result = orders;



        
    //     // Iteramos las ordenes aplicando la regla
    //     for (Order order: orders.keySet() ){
    //         for (OrderDetail orderDetail: orders.get(order)){
    //             Product product = productService.getProductById(orderDetail.getProductId())
    //                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    //             Long followerValue = followerType.getFollowerValue(product, orderDetail.getProductAmount(), this.followerId);
    //             if ( followerValue < 0 ){
    //                 // Devuelvo lista vacia. Es imposible completar la solicitud
    //                 // Ejemplo: Regla producto no debe tener X attribute
    //                 return new HashMap<Order, List<OrderDetail>>();
    //             } else if ( ! (followerValue < this.value) ) {
    //                 // TODO Rearmar el carrito 
    //                 orderDetail.setProductAmount(followerValue);
    //                 Long exceded_quantity = followerValue - ( this.value - 1 );

    //                 // Tengo espacio libre en otra orden para este pedido ?  
    //                 // Si -> Lo sumo al primer pedido que encuentre
    //                 // No -> Creo una orden nueva 
    //                 while (exceded_quantity > 0) {
    //                     Order new_order = new Order();
    //                     new_order.setId(order.getId() + 1);
    //                     new_order.setStatus(order.getStatus());
    //                     new_order.setOrderDate(order.getOrderDate());
    //                     new_order.setUser(order.getUser());


    //                 }
    //             }
    //         }
    //     }

    //     return result;
    // }

    
    
}
