package com.ecommerce.order.dto;

import com.ecommerce.order.enums.PaymentMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    @NotNull
    private long orderId;

    @NotNull
    private String userId;

    @NotNull
    @Min(1)
    private BigDecimal amount;

    @NotNull
    private PaymentMethod method;
}
