package ar.uba.fi.ingsoft1.rules.repository;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import ar.uba.fi.ingsoft1.attributes.repository.AttributeValue;
import ar.uba.fi.ingsoft1.orders.repository.OrderDetail;
import ar.uba.fi.ingsoft1.products.repository.Product;

public class RuleNumericEqual implements Rule {
    
    private Long value;
    private Long followerId;

    public RuleNumericEqual(AbstracRule abstracRule) {
        // aca debería obtener todo lo necesario que tenga la regla abstracta
        this.value = abstracRule.getValue();
        this.followerId = abstracRule.getRulesFollowerId();
    }
    

    @Override
    public HashMap<Long, List<OrderDetail>> applyWithMasterPoduct(Long actOrderId,
            HashMap<Long, List<OrderDetail>> orders) {
        
        // Esto habría que hacerlo siempre al principio de aplicar una regla,
        // así directamente se guardan las listas nuevas.
        // (podrían ser variables locales)
	    List<OrderDetail> actOrderDetails = orders.get(actOrderId);
//        List<OrderDetail> nextOrderDetails = orders.get(nextOrderId);

//        HashMap<Long, List<OrderDetail>> newOrders = orders;
        for (OrderDetail orderDetail: actOrderDetails){
            Product product = orderDetail.getProduct(); 
            if ( product.getMasterProduct().getId() == this.followerId ){
                if ( ! ( orderDetail.getProductAmount() == this.value )) {  
                    // Esta regla no puede resolverse. Devuelve lista de ordenes vacia y se debe avisar a usuario
                    orders.remove(actOrderId);
                    orders.put(actOrderId, new ArrayList<OrderDetail>());
                    
                    break;
                }
            }            
        }

        return orders;
    }

    @Override
    public HashMap<Long, List<OrderDetail>> applyWithProduct(Long actOrderId,
            HashMap<Long, List<OrderDetail>> orders) {

        List<OrderDetail> actOrderDetails = orders.get(actOrderId);
        
        for (OrderDetail orderDetail: actOrderDetails){
            Product product = orderDetail.getProduct(); 
            if ( product.getId() == this.followerId ){
                if ( ! ( orderDetail.getProductAmount() == this.value )) {
                    // Esta regla no puede resolverse. Devuelve lista de ordenes vacia y se debe avisar a usuario
                    orders.remove(actOrderId);
                    orders.put(actOrderId, new ArrayList<OrderDetail>());
                    
                    break;
                }
            }            
        }

        return orders;
    }

    @Override
    public HashMap<Long, List<OrderDetail>> applyWithAttribute(Long actOrderId,
            HashMap<Long, List<OrderDetail>> orders) {
        List<OrderDetail> actOrderDetails = orders.get(actOrderId);
        
        for (OrderDetail orderDetail: actOrderDetails){
            Product product = orderDetail.getProduct(); 
            if ( product.containsAttribute(this.followerId) ){
                Long amount = orderDetail.getProductAmount() ;

                if ( ! ( amount == this.value )) {
                    // Esta regla no puede resolverse. Devuelve lista de ordenes vacia y se debe avisar a usuario
                    orders.remove(actOrderId);
                    orders.put(actOrderId, new ArrayList<OrderDetail>());
                    
                    break;
                }
            }            
        }

        return orders;
    }

    @Override
    public HashMap<Long, List<OrderDetail>> applyWithAttributeValue(Long actOrderId,
            HashMap<Long, List<OrderDetail>> orders) {
        List<OrderDetail> actOrderDetails = orders.get(actOrderId);
        
        for (OrderDetail orderDetail: actOrderDetails){
            Product product = orderDetail.getProduct(); 
            if ( product.containsAttribute(this.followerId) ){
		        AttributeValue attribute = product.getAttribute(this.followerId).orElse(null);
                String value = attribute.getValue();
                if (!(value == null) && !value.isEmpty()) {     // El valor contiene algo
                    if (value.matches("-?\\d+")) {              // El valor es numerico
                        if ( ! ( Long.valueOf(attribute.getValue()) == this.value )) {
                            // Esta regla no puede resolverse. Devuelve lista de ordenes vacia y se debe avisar a usuario
                            orders.remove(actOrderId);
                            orders.put(actOrderId, new ArrayList<OrderDetail>());
                    
                            break;
                        }    
                    }    
                }
            }            
        }

        return orders;
    }
}