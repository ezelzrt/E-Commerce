package ar.uba.fi.ingsoft1.rules.repository;
import ar.uba.fi.ingsoft1.orders.repository.OrderDetail;

import java.util.HashMap;
import java.util.List;

public enum RulesFollowerType {    
    MASTER_PRODUCT {
        // @Override
        // public Long getFollowerValue(Product product, Long amount, Long followerId) {
        //     return ( product.getMasterProductId() == followerId ) ? amount : 0; 
        // }

        @Override
        public HashMap<Long, List<OrderDetail>> verifyWith(Rule rule, Long actOrderId, HashMap<Long, List<OrderDetail>> orders) {
            return rule.applyWithMasterPoduct(actOrderId, orders);
        }
    },
    PRODUCT {
        // @Override
        // public Long getFollowerValue(Product product, Long amount, Long followerId) {
        //     return ( product.getId() == followerId ) ? amount : 0 ;
        // }

        @Override
        public HashMap<Long, List<OrderDetail>> verifyWith(Rule rule, Long actOrderId, HashMap<Long, List<OrderDetail>> orders) {
            return rule.applyWithProduct(actOrderId, orders);
        }
    },
    ATTRIBUTE {
        // @Override
        // public Long getFollowerValue(Product product,Long amount, Long followerId) {
        //     for ( ProductAttribute productAttribute: product.getAttributes() ) {
        //         if (productAttribute.getId() == followerId){
        //             return -1L;
        //         }
        //     }
        //     return 0L;
        // }

        @Override
        public HashMap<Long, List<OrderDetail>> verifyWith(Rule rule, Long actOrderId,
                HashMap<Long, List<OrderDetail>> orders) {
            return rule.applyWithAttribute(actOrderId, orders);
        }
    },
    ATTRIBUTE_VALUE {
        // @Override
        // public Long getFollowerValue(Product product, Long amount, Long followerId) {
        //     ProductAttribute productAttribute = product.getAttributeById(followerId);
        //     return ( productAttribute.getId() == -1 ) ? productAttribute.getValue() : 0;
        // }

        @Override
        public HashMap<Long, List<OrderDetail>> verifyWith(Rule rule, Long actOrderId,
                HashMap<Long, List<OrderDetail>> orders) {
            return rule.applyWithAttributeValue(actOrderId, orders);
        }
    };

/*
    TODO: Comentado temporalmente 
    Sale de los parametros, ya que deberiamos indicar el ID del producto y del atributo
    MASTER_PRODUCT_ATTRIBUTE_RELATION {
        @Override
        public List<Order> getFollowerValue(Product product, Long amount, Long followerId){
            return "Custom value for Master Product Attribute Relation";
        }
    };
*/
    // Devuelve la cantidad contra la que se compara value
    // Si se debe revisar si el valor existe, pero no la cantidad, devuelve -1 o 0
    // Si se debe revisar la cantidad, devuelve 0 o el valor en cuestion 
    // public abstract Long getFollowerValue(Product product, Long amount, Long followerId);

    public abstract HashMap<Long, List<OrderDetail>> verifyWith(Rule rule, Long actOrderId, HashMap<Long, List<OrderDetail>> orders);
}
