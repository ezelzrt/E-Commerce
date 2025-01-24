package ar.uba.fi.ingsoft1.attributes.repository;

import ar.uba.fi.ingsoft1.masterProducts.repository.MasterProduct;
import ar.uba.fi.ingsoft1.products.repository.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    Attribute findById(Attribute attribute);

}

