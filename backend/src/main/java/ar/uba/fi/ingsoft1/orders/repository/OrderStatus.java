package ar.uba.fi.ingsoft1.orders.repository;

import ar.uba.fi.ingsoft1.orders.service.OrderService;

public enum OrderStatus {
    ENTERED{

        @Override
        public OrderStatus nextStatus() {
            return OrderStatus.CONFIRMED;
        }

        @Override
        public Boolean isUserModificable() {
            return true;
        }

        @Override
        public Order manageOrder(OrderService orderService, Order order) {
            return order;
        }
        
    },
    CONFIRMED{
        
        @Override
        public OrderStatus nextStatus() {
            return OrderStatus.IN_PROGRESS;
        }
        
        @Override
        public Boolean isUserModificable() {
            return true;
        }
        
        @Override
        public Order manageOrder(OrderService orderService, Order order) {
            return orderService.confirmOrder(order);
        }

    },
    IN_PROGRESS{

        @Override
        public OrderStatus nextStatus() {
            return OrderStatus.DELIVERED;
        }

        @Override
        public Boolean isUserModificable() {
            return false;
        }
        
        @Override
        public Order manageOrder(OrderService orderService, Order order) {
            return orderService.progressOrder(order);
        }

    },
    DELIVERED {

        @Override
        public OrderStatus nextStatus() {
            return this;
        }
        
        @Override
        public Boolean isUserModificable() {
            return false;
        }
        
        @Override
        public Order manageOrder(OrderService orderService, Order order) {
            return orderService.deliverOrder(order);
        }

    },
    CANCELLED{

        @Override
        public OrderStatus nextStatus() {
            return this;
        }

        @Override
        public Boolean isUserModificable() {
            return false;
        }
        
        @Override
        public Order manageOrder(OrderService orderService, Order order) {
            return orderService.cancelOrder(order);
        }

        @Override
        public Boolean isCancelledStatus(){
            return true;
        }

    };
    
    public abstract OrderStatus nextStatus();
    public abstract Boolean isUserModificable();
    public abstract Order manageOrder(OrderService orderService, Order order);
    
    public Boolean isCancelledStatus(){
        return false;
    }
}
