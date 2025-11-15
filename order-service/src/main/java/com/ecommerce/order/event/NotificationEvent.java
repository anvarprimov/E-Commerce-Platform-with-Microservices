package com.ecommerce.order.event;

import com.ecommerce.order.enums.Channel;
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
    private Channel channel;
}
