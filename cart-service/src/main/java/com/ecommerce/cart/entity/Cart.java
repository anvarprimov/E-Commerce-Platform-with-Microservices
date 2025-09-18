package com.ecommerce.cart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class Cart {
    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private long userId;

//    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
//    private List<CartItem> items = new ArrayList<>();

    @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal tax = BigDecimal.ZERO;

    @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private Timestamp updatedAt;*/
}
