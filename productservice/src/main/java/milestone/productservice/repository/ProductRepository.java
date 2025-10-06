package milestone.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import milestone.productservice.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
