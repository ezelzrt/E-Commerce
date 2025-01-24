package ar.uba.fi.ingsoft1.attributes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long> {
    Attribute findById(Attribute attribute);

}

