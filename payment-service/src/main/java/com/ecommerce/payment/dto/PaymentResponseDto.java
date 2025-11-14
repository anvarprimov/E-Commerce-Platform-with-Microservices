package com.ecommerce.payment.dto;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {
    private Long id;
    private String paymentId;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private String status;
    private String method;
    private String failureReason;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
