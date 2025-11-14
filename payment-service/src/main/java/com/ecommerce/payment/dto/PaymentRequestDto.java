package com.ecommerce.payment.dto;
import com.ecommerce.payment.enums.PaymentMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    @NotNull
    private Long orderId;

    @NotNull
    private Long userId;

    @NotNull
    @Min(1)
    private BigDecimal amount;

    @NotNull
    private PaymentMethod method;
}
