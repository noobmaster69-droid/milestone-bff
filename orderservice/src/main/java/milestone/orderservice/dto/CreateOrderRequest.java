package milestone.orderservice.dto;

public class CreateOrderRequest {
    // DDL shows order-level product_id and quantity (single product per order)
    public Long productId;
    public Integer quantity;
}
