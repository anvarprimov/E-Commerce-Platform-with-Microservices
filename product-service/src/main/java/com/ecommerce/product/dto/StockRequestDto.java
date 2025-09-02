package com.ecommerce.product.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockRequestDto {
    @NotEmpty
    List<StockUnitRequestDto> stockUnitRequestDtoList;
}
