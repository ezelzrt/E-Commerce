package ar.uba.fi.ingsoft1.products.repository;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

import ar.uba.fi.ingsoft1.attributes.repository.AttributeValue;
import ar.uba.fi.ingsoft1.masterProducts.repository.MasterProduct;
import ar.uba.fi.ingsoft1.orders.repository.Order;
import ar.uba.fi.ingsoft1.orders.repository.OrderDetail;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Min(value = 0)
    private Double price;

    @Min(value = 0)
    private Long stock;
    
    @Min(value = 0)
    @Builder.Default
    private Long compromisedStock = 0L;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToMany
    @JoinTable(
            name = "TB_Product_AttributeValue",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_value_id")
    )
    private List<AttributeValue> attributeValues;

    @ManyToOne
    @JoinColumn(name = "master_product_id", nullable = false)
    @JsonBackReference
    private MasterProduct masterProduct;

    @OneToMany(mappedBy = "product")
    @JsonBackReference
    private List<OrderDetail> orderDetails;

    public Long addStock(Long addStock){
        this.stock += addStock;
        return stock;
    }
    
    public Long addCompromisedStock(Long addCompromisedStock) {
        this.stock -= addCompromisedStock;
        this.compromisedStock += addCompromisedStock;
        return compromisedStock;
    }

    // public Long getStock(){
    //     return stock;
    // }

    public Long getCompromisedStock(){
        return compromisedStock;
    }

    // public void setStock(Long stock){
    //     this.stock = stock;
    // }

    public void setCompromisedStock(Long comprised_stock){
        this.compromisedStock = comprised_stock;
    }

    // public long getMasterProductId(){
    //     return this.master_product_id;
    // }

    public Optional<AttributeValue> getAttribute(Long id){
        if (containsAttribute(id)){
            return Optional.of(getAttributeValues().get(Long.valueOf(id).intValue()));
        } else {
            return Optional.empty();
        }
    }
    
        // Pre: Id de atributo
    // Post: ProductAttribute valido o dummy ProductAttribute indicando inexistencia
    public boolean containsAttribute(Long id){
        for ( AttributeValue productAttribute: this.attributeValues ){
            if ( productAttribute.getId() == id ){
                return true;
            }
        }
        return false;
    }

/* 
    public List<ProductAttribute> getAttributes(){
        return this.attributes;
    }

    public Optional<ProductAttribute> getAttribute(Long id){
        if (containsAttribute(id)){
            return Optional.of(getAttributes().get(Long.valueOf(id).intValue()));
        } else {
            return Optional.empty();
        }
    }

*/
}
