package com.ecommerce.order.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequestDto {
    @NotNull
    private long productId;
    @NotBlank
    private String productName;
    @NotNull
    @Min(1)
    private Integer quantity;
    @NotNull @Positive
    private BigDecimal unitPrice;
}
