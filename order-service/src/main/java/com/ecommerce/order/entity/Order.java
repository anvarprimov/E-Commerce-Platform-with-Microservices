package com.ecommerce.order.entity;

import com.ecommerce.order.enums.OrderStatus;
import com.ecommerce.order.enums.PaymentMethod;
import com.ecommerce.order.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long paymentId;

    @Column(nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private OrderStatus status;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false, scale = 2, precision = 19)
    private BigDecimal subtotal;

    @Column(nullable = false, scale = 2, precision = 19)
    private BigDecimal shippingFee;

    @Column(nullable = false, scale = 2, precision = 19)
    private BigDecimal tax;

    @Column(nullable = false, scale = 2, precision = 19)
    private BigDecimal total;

    private String notes;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private Timestamp updatedAt;

    public void addItem(OrderItem item) {
        item.setOrder(this);
        items.add(item);
    }
}

