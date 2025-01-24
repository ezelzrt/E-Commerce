package ar.uba.fi.ingsoft1.masterProducts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterProductRepository extends JpaRepository<MasterProduct, Long> {



}

