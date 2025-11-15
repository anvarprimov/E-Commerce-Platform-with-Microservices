package com.ecommerce.notification.event;

import com.ecommerce.notification.enums.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private String userId;
    private String title;
    private String body;
    private Channel channel;
}
