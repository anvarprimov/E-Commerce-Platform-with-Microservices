package com.ecommerce.order.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityDto {
    @NotNull
    private long productId;
    @NotNull
    @Min(1)
    private Integer quantity;
}
