package com.ecommerce.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private String userId;
    private String title;
    private String body;
    private String channel;
}
