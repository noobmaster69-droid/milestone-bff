package milestone.orderservice.client;

import java.math.BigDecimal;

public class ProductDto {
    private Integer productId;
    private String name;
    private String description;
    private BigDecimal basePrice;

    public Integer getProductId() { return productId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getBasePrice() { return basePrice; }
}
