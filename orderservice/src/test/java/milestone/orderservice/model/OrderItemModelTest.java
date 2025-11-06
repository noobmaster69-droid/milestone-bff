package milestone.orderservice.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class OrderItemModelTest {

    @Test
    void getters_and_setters() {
        OrderItem oi = new OrderItem();
        oi.setProductId(3);
        oi.setQuantity(4);
        oi.setUnitPrice(BigDecimal.valueOf(2.5));
        assertEquals(3, oi.getProductId());
        assertEquals(4, oi.getQuantity());
        assertEquals(BigDecimal.valueOf(2.5), oi.getUnitPrice());
    }
}
