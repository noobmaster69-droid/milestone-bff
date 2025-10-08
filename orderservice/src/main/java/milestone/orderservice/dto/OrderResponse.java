package milestone.orderservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderResponse {
    public Long orderId;
    public Long productId;
    public Integer quantity;
    public String status;
    public BigDecimal totalPrice;
    public LocalDateTime createdAt;
}
