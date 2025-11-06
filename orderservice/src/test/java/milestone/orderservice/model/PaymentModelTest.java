package milestone.orderservice.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

public class PaymentModelTest {

    @Test
    void onCreate_setsCreatedAt() {
        Payment p = new Payment();
        p.setOrderId(10L);
        p.setAmount(BigDecimal.valueOf(5));
        assertNull(p.getCreatedAt());
        p.onCreate();
        assertNotNull(p.getCreatedAt());
    }
}
