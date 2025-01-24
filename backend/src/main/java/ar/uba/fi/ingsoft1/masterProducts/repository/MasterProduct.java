package ar.uba.fi.ingsoft1.masterProducts.repository;

import ar.uba.fi.ingsoft1.attributes.repository.Attribute;
import ar.uba.fi.ingsoft1.attributes.repository.AttributeValue;
import ar.uba.fi.ingsoft1.orders.repository.OrderDetail;
import ar.uba.fi.ingsoft1.products.repository.Product;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_MasterProduct")
public class MasterProduct {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    @Column(name = "base_price", nullable = false)
    private double basePrice;

    @ManyToMany
    @JsonManagedReference
    @JoinTable(
            name = "TB_MasterProduct_Attribute",
            joinColumns = @JoinColumn(name = "masterproduct_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_id")
    )
    private Set<Attribute> attributes;

    @ManyToMany
    @JsonManagedReference
    @JoinTable(
            name = "TB_MasterProduct_Attribute_Relation",
            joinColumns = @JoinColumn(name = "masterproduct_id"),
            inverseJoinColumns = @JoinColumn(name = "value_id")
    )
    private Set<AttributeValue> attributeValues;

    @OneToMany(mappedBy = "masterProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Product> products;
}
