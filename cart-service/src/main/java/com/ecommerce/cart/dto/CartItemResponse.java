package com.ecommerce.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private String name;
    private long productId;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal lineTotal;
}
