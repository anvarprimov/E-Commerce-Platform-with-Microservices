package com.ecommerce.payment.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String PAYMENT_EXCHANGE = "payment.exchange";
    public static final String PAYMENT_STATUS_QUEUE = "payment.status.queue";
    public static final String PAYMENT_STATUS_ROUTING_KEY = "payment.status.#";

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE, true, false);
    }

    @Bean
    public Queue paymentStatusQueue() {
        return QueueBuilder.durable(PAYMENT_STATUS_QUEUE).build();
    }

    @Bean
    public Binding paymentStatusBinding() {
        return BindingBuilder
                .bind(paymentStatusQueue())
                .to(paymentExchange())
                .with(PAYMENT_STATUS_ROUTING_KEY);
    }
}
