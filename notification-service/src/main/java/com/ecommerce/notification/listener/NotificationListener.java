package com.ecommerce.notification.listener;

import com.ecommerce.notification.config.RabbitMQConfig;
import com.ecommerce.notification.dto.NotificationEvent;
import com.ecommerce.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {
    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void onMessage(NotificationEvent evt) {
        notificationService.handleEvent(evt);
    }
}
