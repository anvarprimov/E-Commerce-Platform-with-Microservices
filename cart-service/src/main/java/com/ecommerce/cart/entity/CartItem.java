package com.ecommerce.cart.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private long productId;

    @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private int quantity;

    @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal lineTotal;
}

