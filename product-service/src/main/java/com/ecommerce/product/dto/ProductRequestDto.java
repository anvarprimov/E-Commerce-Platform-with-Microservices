package com.ecommerce.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @Min(1)
    @NotNull
    private BigDecimal price;
    @Min(1)
    @NotNull
    private Integer quantity;
    private String brand;
}
