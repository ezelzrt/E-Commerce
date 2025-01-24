package ar.uba.fi.ingsoft1.orders.controller;

public class ProductOrder {
    private Long productId;
    private Long amount;

    public Long getProductId() { return productId; }
    public Long getAmount() { return amount; }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}