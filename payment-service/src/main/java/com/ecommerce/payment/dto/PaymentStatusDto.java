package com.ecommerce.payment.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusDto {
    private String paymentId;
    private String status;
    private String failureReason;
}
