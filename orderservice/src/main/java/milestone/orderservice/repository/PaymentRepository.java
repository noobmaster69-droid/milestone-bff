package milestone.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import milestone.orderservice.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}
