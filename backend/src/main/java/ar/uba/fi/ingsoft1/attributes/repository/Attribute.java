package ar.uba.fi.ingsoft1.attributes.repository;

import java.util.List;
import java.util.Set;

import ar.uba.fi.ingsoft1.masterProducts.repository.MasterProduct;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_Attributes")
public class Attribute {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    @ManyToMany(mappedBy = "attributes")
    @JsonBackReference
    private List<MasterProduct> masterProducts;

    @OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<AttributeValue> values;
}

