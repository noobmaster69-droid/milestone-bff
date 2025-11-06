package milestone.orderservice.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class OrderModelTest {

    @Test
    void lifecycle_onCreate_and_onUpdate() {
        Order o = new Order();
        o.setProductId(1L);
        o.setQuantity(2);
        o.setTotalPrice(BigDecimal.TEN);
        assertNull(o.getCreatedAt());
        o.onCreate();
        assertNotNull(o.getCreatedAt());
        assertNotNull(o.getUpdatedAt());

        LocalDateTime before = o.getUpdatedAt();
        o.onUpdate();
        assertTrue(o.getUpdatedAt().isAfter(before) || o.getUpdatedAt().isEqual(before));
    }
}
