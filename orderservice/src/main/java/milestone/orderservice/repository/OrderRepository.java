package milestone.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import milestone.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // no userId column in new DDL; keep placeholder if still used elsewhere
    List<Order> findByProductId(Long productId);
    List<Order> findByUserId(Integer userId);
}
