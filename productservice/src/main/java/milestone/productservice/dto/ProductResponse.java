package milestone.productservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductResponse {
    private Integer productId;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private Integer categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductResponse(Integer productId, String name, String description, BigDecimal basePrice, Integer categoryId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getProductId() { return productId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getBasePrice() { return basePrice; }
    public Integer getCategoryId() { return categoryId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
