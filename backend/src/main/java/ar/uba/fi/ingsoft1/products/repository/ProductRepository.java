package ar.uba.fi.ingsoft1.products.repository;


import java.util.List;
import java.util.Optional;

import ar.uba.fi.ingsoft1.masterProducts.repository.MasterProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository  extends JpaRepository<Product, Long> {
    
    Optional<List<Product>> findByMasterProduct(MasterProduct masterProduct);

}

