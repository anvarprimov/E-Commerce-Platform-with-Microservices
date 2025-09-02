package com.ecommerce.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, scale = 2, precision = 19)
    private BigDecimal unitPrice;

    @Column(nullable = false, scale = 2, precision = 19)
    private BigDecimal lineTotal;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "order_id")
//    @ToString.Exclude @EqualsAndHashCode.Exclude
    private Order order;
}
