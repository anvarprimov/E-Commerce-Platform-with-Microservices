package com.ecommerce.notification.entity;

import com.ecommerce.notification.enums.Channel;
import com.ecommerce.notification.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

@Entity
@Table(name="notifications",
        indexes = {@Index(name="idx_user_status", columnList = "userId,status")})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, updatable = false)
    private String userId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String body;

    @Enumerated(EnumType.STRING)
    private Channel channel;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    private OffsetDateTime readAt;
    private OffsetDateTime sentAt;
}
