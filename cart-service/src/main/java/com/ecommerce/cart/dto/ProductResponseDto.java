package com.ecommerce.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String brand;
    private boolean active;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
