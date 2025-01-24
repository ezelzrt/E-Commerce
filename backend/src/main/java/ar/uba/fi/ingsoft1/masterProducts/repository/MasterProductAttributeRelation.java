package ar.uba.fi.ingsoft1.masterProducts.repository;

import ar.uba.fi.ingsoft1.attributes.repository.Attribute;
import ar.uba.fi.ingsoft1.attributes.repository.AttributeValue;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "TB_MasterProduct_Attribute_Relation")
public class MasterProductAttributeRelation {
    @EmbeddedId
    private MasterProductAttributeRelationId id;

    @ManyToOne
    @MapsId("masterproductId")
    @JsonBackReference
    @JoinColumn(name = "masterproduct_id")
    private MasterProduct masterProduct;

    @ManyToOne
    @MapsId("attributeId")
    @JsonBackReference
    @JoinColumn(name = "attribute_id")
    private Attribute attribute;

    @ManyToOne
    @MapsId("valueId")
    @JsonBackReference
    @JoinColumn(name = "value_id")
    private AttributeValue attributeValue;

}
