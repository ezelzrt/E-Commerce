package ar.uba.fi.ingsoft1.rules.repository;
import java.util.List;
import java.util.HashMap;

import ar.uba.fi.ingsoft1.orders.repository.OrderDetail;

public interface Rule {
    //    public List<Order> apply(Order order);
    // public HashMap<Long, List<OrderDetail>> apply(Long currentOrderId, List<OrderDetail> orders);
    // public HashMap<Long, List<OrderDetail>> apply(Long currentOrderId, HashMap<Long, List<OrderDetail>> orders);

    public HashMap<Long, List<OrderDetail>> applyWithMasterPoduct(Long actOrderId, HashMap<Long, List<OrderDetail>> orders);

    public HashMap<Long, List<OrderDetail>> applyWithProduct(Long actOrderId, HashMap<Long, List<OrderDetail>> orders);

    public HashMap<Long, List<OrderDetail>> applyWithAttribute(Long actOrderId, HashMap<Long, List<OrderDetail>> orders);

    public HashMap<Long, List<OrderDetail>> applyWithAttributeValue(Long actOrderId, HashMap<Long, List<OrderDetail>> orders);
    
}