package milestone.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import milestone.orderservice.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}
