package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private long id;
    private long userId;
    private List<OrderItemResponseDto> items;
    private BigDecimal total;
    private String notes;
    private Timestamp createdAt;
}
