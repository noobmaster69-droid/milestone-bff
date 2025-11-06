package milestone.productservice.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class ProductResponseTest {

    @Test
    void constructor_and_getters() {
        LocalDateTime now = LocalDateTime.now();
        ProductResponse r = new ProductResponse(1, "n", "d", BigDecimal.valueOf(9.99), 2, now, now.plusDays(1));
        assertEquals(1, r.getProductId());
        assertEquals("n", r.getName());
        assertEquals("d", r.getDescription());
        assertEquals(BigDecimal.valueOf(9.99), r.getBasePrice());
        assertEquals(2, r.getCategoryId());
        assertEquals(now, r.getCreatedAt());
    }
}
