package ar.uba.fi.ingsoft1.rules.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ar.uba.fi.ingsoft1.orders.repository.OrderDetail;
import ar.uba.fi.ingsoft1.products.repository.Product;
import ar.uba.fi.ingsoft1.attributes.repository.AttributeValue;

public class RuleNumericLowerEqual implements Rule {
    private Long value;
    private Long followerId;

    public RuleNumericLowerEqual(AbstracRule abstracRule) {
        // aca debería obtener todo lo necesario que tenga la regla abstracta
        this.value = abstracRule.getValue();
        this.followerId = abstracRule.getRulesFollowerId();
    }

    private Long findOrderWithoutMasterProductId(HashMap<Long, List<OrderDetail>> orders, Long nextOrderId ) {
        boolean found = false;
        Long orderId = nextOrderId;     // Empezamos revisando desde nextOrder ( la anterior es la orden valida para esta regla )

        int counter;
        List<OrderDetail> actualOrders;
        while (!found && !orders.get(orderId).isEmpty()) {
            actualOrders = orders.get(orderId);

            counter = 0;
            while ( isMasterProductNotPresentInOrder(counter, actualOrders) ){
                counter += 1;
            }

            if ( counter == actualOrders.size() ){
                found = true;   // La orden actual no tiene el producto
            } else {
                orderId += 1;
            }

            if ((orderId + 1) == orders.keySet().size() ){
                orders.put(orderId, new ArrayList<>());
            }
        }

        if (!found){
            orders.put(orderId, new ArrayList<OrderDetail>() );
        }

        return orderId;
    }

    // aux findOrderWithoutMasterProductId. Chequea condicion en lista
    private boolean isMasterProductNotPresentInOrder(int counter, List<OrderDetail> orderDetails) {
        return ( counter < orderDetails.size() ) && ( orderDetails.get(counter).getProduct().getMasterProduct().getId() != this.followerId ) ;
    }
    
    private Long findOrderWithoutProductId(HashMap<Long, List<OrderDetail>> orders, Long nextOrderId ) {
        boolean found = false;
        Long orderId = nextOrderId;     // Empezamos revisando desde nextOrder ( la anterior es la orden valida para esta regla )

        int counter;
        List<OrderDetail> actualOrders;
        while (!found && !orders.get(orderId).isEmpty()) {
            actualOrders = orders.get(orderId);

            counter = 0;
            while ( isProductNotPresentInOrder(counter, actualOrders) ){
                counter += 1;
            }

            if ( counter == actualOrders.size() ){
                found = true;   // La orden actual no tiene el producto
            } else {
                orderId += 1;
            }

            if ((orderId + 1) == orders.keySet().size() ){
                orders.put(orderId, new ArrayList<>());
            }
        }

        if (!found){
            orders.put(orderId, new ArrayList<OrderDetail>() );
        }

        return orderId;
    }

    // aux findOrderWithoutProductId. Chequea condicion en lista
    private boolean isProductNotPresentInOrder(int counter, List<OrderDetail> orderDetails ) {
        return ( counter < orderDetails.size() ) && ( orderDetails.get(counter).getProduct().getId() != this.followerId ) ;
    }

    private Long findOrderWithoutAttribute(HashMap<Long, List<OrderDetail>> orders, Long nextOrderId ) {
        boolean found = false;
        Long orderId = nextOrderId;     // Empezamos revisando desde nextOrder ( la anterior es la orden valida para esta regla )

        int counter;
        List<OrderDetail> actualOrders;
        while (!found && !orders.get(orderId).isEmpty()) {
            actualOrders = orders.get(orderId);

            counter = 0;
            while ( isAttributeNotPresentInOrder(counter, actualOrders) ){
                counter += 1;
            }

            if ( counter == actualOrders.size() ){
                found = true;   // La orden actual no tiene el producto
            } else {
                orderId += 1;
            }

            if ((orderId + 1) == orders.keySet().size() ){
                orders.put(orderId, new ArrayList<>());
            }
        }

        if (!found){
            orders.put(orderId, new ArrayList<OrderDetail>() );
        }

        return orderId;
    }

    private boolean isAttributeNotPresentInOrder(int counter, List<OrderDetail> orderDetails ) {
        return ( counter < orderDetails.size() ) && ( orderDetails.get(counter).getProduct().containsAttribute(this.followerId) ) ;
    }

//    @Autowired
//    void RuleNumericLowerController(AbstracRule abstracRule, ProductService productService){
//        // this.followerType = rule.getRulesFollowerType();
//        this.followerId = abstracRule.getRulesFollowerId();
//        this.value = Integer.parseInt(abstracRule.getValue());
//        this.productService = productService;
//    }

    @Override
    public HashMap<Long, List<OrderDetail>> applyWithMasterPoduct(Long actOrderId,
            HashMap<Long, List<OrderDetail>> orders) {
        
        // Esto habría que hacerlo siempre al principio de aplicar una regla,
        // así directamente se guardan las listas nuevas.
        // (podrían ser variables locales)
        Long nextOrderId = actOrderId + 1L;

	    List<OrderDetail> actOrderDetails = orders.get(actOrderId);
//        List<OrderDetail> nextOrderDetails = orders.get(nextOrderId);

//        HashMap<Long, List<OrderDetail>> newOrders = orders;
        for (OrderDetail orderDetail: actOrderDetails){
            Product product = orderDetail.getProduct(); 
            if ( product.getMasterProduct().getId() == this.followerId ){
		        Long amount = orderDetail.getProductAmount() ;

                if ( ! ( amount <= this.value )) {
		            orderDetail.setProductAmount(this.value);
                    
                    // Encontrar orden sin dicho producto y agregarlo
                    Long availableOrderId = findOrderWithoutMasterProductId(orders, nextOrderId);
                    orders.get(availableOrderId).add( 
                        OrderDetail.builder()
                        .product(product)
                        .productAmount(amount - (this.value))
                        .productPrice(product.getPrice())
                        .build()
                    );
                }
            }            
        }
        // Actualizo las "ordenes" con los nuevos orderDetails
//        newOrders.put(actOrderId, actOrderDetails);
//        newOrders.put(nextOrderId, nextOrderDetails);
        return orders;
    }

    @Override
    public HashMap<Long, List<OrderDetail>> applyWithProduct(Long actOrderId,
            HashMap<Long, List<OrderDetail>> orders) {

        Long nextOrderId = actOrderId + 1L;

        List<OrderDetail> actOrderDetails = orders.get(actOrderId);
        
        for (OrderDetail orderDetail: actOrderDetails){
            Product product = orderDetail.getProduct(); 
            if ( product.getId() == this.followerId ){
		        Long amount = orderDetail.getProductAmount() ;

                if ( ! ( amount <= this.value )) {
		            orderDetail.setProductAmount(this.value);
                    
                    // Encontrar orden sin dicho producto y agregarlo
                    Long availableOrderId = findOrderWithoutProductId(orders, nextOrderId);
                    orders.get(availableOrderId).add( 
                        OrderDetail.builder()
                        .product(product)
                        .productAmount(amount - (this.value))
                        .productPrice(product.getPrice())
                        .build()
                    );
                }
            }            
        }

        return orders;
    }

    @Override
    public HashMap<Long, List<OrderDetail>> applyWithAttribute(Long actOrderId,
            HashMap<Long, List<OrderDetail>> orders) {
        Long nextOrderId = actOrderId + 1L;

        List<OrderDetail> actOrderDetails = orders.get(actOrderId);
        
        for (OrderDetail orderDetail: actOrderDetails){
            Product product = orderDetail.getProduct(); 
            if ( product.containsAttribute(this.followerId) ){
                Long amount = orderDetail.getProductAmount() ;

                if ( ! ( amount < this.value )) {
		            orderDetail.setProductAmount(this.value);
                    
                    // Encontrar orden sin dicho producto y agregarlo
                    Long availableOrderId = findOrderWithoutAttribute(orders, nextOrderId);
                    orders.get(availableOrderId).add( 
                        OrderDetail.builder()
                        .product(product)
                        .productAmount(amount - (this.value))
                        .productPrice(product.getPrice())
                        .build()
                    );
                }
            }            
        }

        return orders;
    }

    @Override
    public HashMap<Long, List<OrderDetail>> applyWithAttributeValue(Long actOrderId,
            HashMap<Long, List<OrderDetail>> orders) {
        Long nextOrderId = actOrderId + 1L;

        List<OrderDetail> actOrderDetails = orders.get(actOrderId);
        
        for (OrderDetail orderDetail: actOrderDetails){
            Product product = orderDetail.getProduct(); 
            if ( product.containsAttribute(this.followerId) ){
		        AttributeValue attribute = product.getAttribute(this.followerId).orElse(null);
                String value = attribute.getValue();
                if (!(value == null) && !value.isEmpty()) {     // El valor contiene algo
                    if (value.matches("-?\\d+")) {              // El valor es numerico
                        if ( ! ( Long.valueOf(attribute.getValue()) <= this.value )) {
                            Long amount = orderDetail.getProductAmount() ;
                            orderDetail.setProductAmount(this.value - 1L);
                            
                            // Encontrar orden sin dicho producto y agregarlo
                            Long availableOrderId = findOrderWithoutAttribute(orders, nextOrderId);
                            orders.get(availableOrderId).add( 
                                OrderDetail.builder()
                                .product(product)
                                .productAmount(amount - (this.value - 1L))
                                .productPrice(product.getPrice())
                                .build()
                            );
                        }    
                    }    
                }
            }            
        }

        return orders;
    }

}
