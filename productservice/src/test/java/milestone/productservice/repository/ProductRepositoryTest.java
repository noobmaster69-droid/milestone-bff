package milestone.productservice.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import milestone.productservice.model.Product;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void saveAndFindById() {
        Product p = Product.builder()
                .name("T")
                .description("D")
                .basePrice(BigDecimal.valueOf(3.14))
                .categoryId(9)
                .build();

        Product saved = productRepository.save(p);
        assertNotNull(saved.getProductId());

        Optional<Product> found = productRepository.findById(saved.getProductId());
        assertTrue(found.isPresent());
        assertEquals("T", found.get().getName());
    }
}
