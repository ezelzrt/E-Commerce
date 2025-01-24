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

public class RuleSum implements Rule {
    
    // private final OrderService orderService;
    
    private Long value;
    private Long followerId;

    public RuleSum(AbstracRule abstracRule) {
        // aca debería obtener todo lo necesario que tenga la regla abstracta
        this.value = abstracRule.getValue();
        this.followerId = abstracRule.getRulesFollowerId();
    }

    @Override
    public HashMap<Long, List<OrderDetail>> applyWithMasterPoduct(Long actOrderId,
            HashMap<Long, List<OrderDetail>> orders) {
        
        // Regla: La cantidad de masterproduct tipo follower_id < this.value 
        Long nextOrderId = actOrderId + 1L;

	    List<OrderDetail> actOrderDetails = orders.get(actOrderId);
        Long partial_sum = 0L;
        int i = 0;
        while (i < actOrderDetails.size()){
            OrderDetail orderDetail = actOrderDetails.get(i);
            Product product = orderDetail.getProduct(); 
            if ( product.getMasterProduct().getId() == this.followerId ){
		        partial_sum += orderDetail.getProductAmount() ;

                if ( ! ( partial_sum < this.value )) {
                    // Todas las ordenes desde la actual en adelante tienen que quitarse de esta orden y colocarse en la siguiente
		            if (orders.get(nextOrderId) == null){
                        orders.put(nextOrderId, new ArrayList<OrderDetail>());
                    }
                    List<OrderDetail> nextOrderDetails = orders.get(nextOrderId);
                    for (int ii=actOrderDetails.size() - 1; i <= ii; ii--){
                        nextOrderDetails.add(actOrderDetails.remove(ii)); 
                    }
                }
            }
            i++;
        }

        return orders;
    }

    @Override
    public HashMap<Long, List<OrderDetail>> applyWithProduct(Long actOrderId,
            HashMap<Long, List<OrderDetail>> orders) {

        Long nextOrderId = actOrderId + 1L;
	    List<OrderDetail> actOrderDetails = orders.get(actOrderId);
        Long partial_sum = 0L;
        int i = 0;
        while (i < actOrderDetails.size()){
            OrderDetail orderDetail = actOrderDetails.get(i);
            Product product = orderDetail.getProduct(); 
            if ( product.getId() == this.followerId ){
		        partial_sum += orderDetail.getProductAmount() ;

                if ( ! ( partial_sum < this.value )) {
                    // Todas las ordenes desde la actual en adelante tienen que quitarse de esta orden y colocarse en la siguiente
		            if (orders.get(nextOrderId) == null){
                        orders.put(nextOrderId, new ArrayList<OrderDetail>());
                    }
                    List<OrderDetail> nextOrderDetails = orders.get(nextOrderId);
                    for (int ii=actOrderDetails.size() - 1; i <= ii; ii--){
                        nextOrderDetails.add(actOrderDetails.remove(ii)); 
                    }
                }
            }
            i++;
        }
                
        return orders;
    }

    @Override
    public HashMap<Long, List<OrderDetail>> applyWithAttribute(Long actOrderId,
            HashMap<Long, List<OrderDetail>> orders) {
        Long nextOrderId = actOrderId + 1L;
	    List<OrderDetail> actOrderDetails = orders.get(actOrderId);
        Long partial_sum = 0L;
        int i = 0;
        while (i < actOrderDetails.size()){
            OrderDetail orderDetail = actOrderDetails.get(i);
            Product product = orderDetail.getProduct(); 
            if ( product.containsAttribute(this.followerId) ){
		        partial_sum += orderDetail.getProductAmount() ;

                if ( ! ( partial_sum < this.value )) {
                    // Todas las ordenes desde la actual en adelante tienen que quitarse de esta orden y colocarse en la siguiente
		            if (orders.get(nextOrderId) == null){
                        orders.put(nextOrderId, new ArrayList<OrderDetail>());
                    }
                    List<OrderDetail> nextOrderDetails = orders.get(nextOrderId);
                    for (int ii=actOrderDetails.size() - 1; i <= ii; ii--){
                        nextOrderDetails.add(actOrderDetails.remove(ii)); 
                    }
                }
            }
            i++;
        }

        return orders;
    }

    @Override
    public HashMap<Long, List<OrderDetail>> applyWithAttributeValue(Long actOrderId,
            HashMap<Long, List<OrderDetail>> orders) {

        Long nextOrderId = actOrderId + 1L;
	    List<OrderDetail> actOrderDetails = orders.get(actOrderId);
        Long partial_sum = 0L;
        int i = 0;
        while (i < actOrderDetails.size()){
            OrderDetail orderDetail = actOrderDetails.get(i);
            Product product = orderDetail.getProduct(); 
            if ( product.containsAttribute(this.followerId) ){
		        AttributeValue attribute = product.getAttribute(this.followerId).orElse(null);
                String value = attribute.getValue();
                if (!(value == null) && !value.isEmpty()) {
                    if (value.matches("-?\\d+")) {
                        partial_sum += Long.valueOf(value);
                        if ( ! ( partial_sum < this.value )) {
                            // Todas las ordenes desde la actual en adelante tienen que quitarse de esta orden y colocarse en la siguiente
                            if (orders.get(nextOrderId) == null){
                                orders.put(nextOrderId, new ArrayList<OrderDetail>());
                            }
                            List<OrderDetail> nextOrderDetails = orders.get(nextOrderId);
                            for (int ii=actOrderDetails.size() - 1; i <= ii; ii--){
                                nextOrderDetails.add(actOrderDetails.remove(ii)); 
                            }
                        }
                    }
                }
            }
            i++;
        }

        return orders;
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
