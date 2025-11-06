package milestone.productservice.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    void builder_and_lifecycleMethods() {
        Product p = Product.builder()
                .name("X")
                .description("desc")
                .basePrice(BigDecimal.valueOf(1.23))
                .categoryId(5)
                .build();

        assertNull(p.getCreatedAt());
        p.onCreate();
        assertNotNull(p.getCreatedAt());
        assertNotNull(p.getUpdatedAt());

        LocalDateTime before = p.getUpdatedAt();
        p.onUpdate();
        assertTrue(p.getUpdatedAt().isAfter(before) || p.getUpdatedAt().isEqual(before));
    }
}
