package com.ecommerce.payment.event;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusChangedEvent {
    private long id;
    private Long orderId;
    private Long userId;
    private String status;
    private String failureReason;
}
