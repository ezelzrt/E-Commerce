package ar.uba.fi.ingsoft1.orders.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository  extends JpaRepository<OrderDetail, Long> {

	List<OrderDetail> findByOrderId(Long orderId);
}

