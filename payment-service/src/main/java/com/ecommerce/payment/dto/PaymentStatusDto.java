package com.ecommerce.payment.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusDto {
    private long id;
    private String status;
    private String failureReason;
}
