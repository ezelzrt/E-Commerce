package ar.uba.fi.ingsoft1.attributes.repository;

import ar.uba.fi.ingsoft1.masterProducts.repository.MasterProductAttributeRelation;
import ar.uba.fi.ingsoft1.products.repository.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "TB_Attribute_Values")
public class AttributeValue {
    @Id
    @GeneratedValue
    private Long id;

    private String value;

    @ManyToOne
    @JoinColumn(name = "attribute_id")
    @JsonBackReference
    private Attribute attribute;

    @OneToMany(mappedBy = "attributeValue")
    @JsonManagedReference
    private List<MasterProductAttributeRelation> masterProductAttributeRelations;

    @ManyToMany(mappedBy = "attributeValues")
    @JsonBackReference
    private List<Product> products;
}

