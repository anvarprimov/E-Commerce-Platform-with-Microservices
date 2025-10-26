package com.ecommerce.notification.dto;

import com.ecommerce.notification.enums.Channel;
import com.ecommerce.notification.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private long id;
    private String userId;
    private String title;
    private String body;
    private Status status;
    private Timestamp createdAt;
    private OffsetDateTime readAt;
    private OffsetDateTime sentAt;
}
