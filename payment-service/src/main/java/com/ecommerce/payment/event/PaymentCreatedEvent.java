package com.ecommerce.payment.event;

import java.math.BigDecimal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreatedEvent {
    private Long id;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
}
