package com.ecommerce.notification.amqp;

import com.ecommerce.notification.dto.NotificationEvent;
import com.ecommerce.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener {
    private final NotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void onMessage(NotificationEvent evt) {
        // idempotent store + send async
        notificationService.handleEvent(evt);
    }
}
