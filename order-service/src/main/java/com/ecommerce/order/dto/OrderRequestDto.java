package com.ecommerce.order.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    @NotEmpty
    private List<QuantityDto> items;
    private String notes;
}
