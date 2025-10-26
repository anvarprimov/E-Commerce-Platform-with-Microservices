package com.ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEvent {
    private String userId;
    private String title;
    private String body;
    private String channel;
}
