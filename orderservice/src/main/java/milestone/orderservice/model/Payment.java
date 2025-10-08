package milestone.orderservice.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Long orderId;

    private BigDecimal amount;

    private String status; // SUCCESS or FAILED

    private String transactionId;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { if (this.createdAt == null) this.createdAt = LocalDateTime.now(); }
}
