package com.ecommerce.order.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    @NotNull
    private long userId;
    @NotEmpty
    private List<OrderItemRequestDto> itemRequestDtoList;
}
