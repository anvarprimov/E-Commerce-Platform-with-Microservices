package com.ecommerce.payment.event;
import com.ecommerce.payment.enums.PaymentStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusEvent {
    private long paymentId;
    private long orderId;
    private String userId;
    private PaymentStatus status;
    private String failureReason;
}
