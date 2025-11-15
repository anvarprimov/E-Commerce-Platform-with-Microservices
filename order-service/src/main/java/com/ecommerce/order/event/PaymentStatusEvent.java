package com.ecommerce.order.event;

import com.ecommerce.order.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusEvent {
    private long paymentId;
    private Long orderId;
    private String userId;
    private PaymentStatus status;
    private String failureReason;
}
