package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {
    private long id;
    private long orderId;
    private String userId;
    private BigDecimal amount;
    private String status;
    private String method;
    private String failureReason;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
