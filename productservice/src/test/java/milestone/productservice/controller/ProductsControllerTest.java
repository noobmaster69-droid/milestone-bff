package milestone.productservice.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

// existing controller under test is injected via @InjectMocks
import milestone.productservice.model.Product;
import milestone.productservice.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductsControllerTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductsController controller;

    Product p1;

    @BeforeEach
    void setup() {
        p1 = Product.builder()
                .productId(1)
                .name("Widget")
                .description("desc")
                .basePrice(BigDecimal.valueOf(9.99))
                .categoryId(10)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void getAll_returnsProducts() {
        when(productRepository.findAll()).thenReturn(List.of(p1));
        ResponseEntity<?> resp = controller.getAll();
    assertEquals(200, resp.getStatusCode().value());
        var list = (java.util.List<?>) resp.getBody();
        assertEquals(1, list.size());
    }

    @Test
    void getById_found() {
        when(productRepository.findById(1)).thenReturn(Optional.of(p1));
        ResponseEntity<?> resp = controller.getById(1);
    assertEquals(200, resp.getStatusCode().value());
    }

    @Test
    void getById_notFound() {
        when(productRepository.findById(2)).thenReturn(Optional.empty());
        ResponseEntity<?> resp = controller.getById(2);
    assertEquals(404, resp.getStatusCode().value());
    }
}
