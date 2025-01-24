package ar.uba.fi.ingsoft1.orders.repository;

import ar.uba.fi.ingsoft1.auth.repository.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser_Id(Long userId);
}

