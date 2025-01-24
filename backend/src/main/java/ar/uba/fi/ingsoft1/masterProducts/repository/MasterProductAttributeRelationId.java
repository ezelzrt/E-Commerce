package ar.uba.fi.ingsoft1.masterProducts.repository;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class MasterProductAttributeRelationId implements Serializable {

    private Long masterproductId;
    private Long attributeId;
    private Long valueId;
    
}
