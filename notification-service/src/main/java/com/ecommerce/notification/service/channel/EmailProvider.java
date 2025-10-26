package com.ecommerce.notification.service.channel;

import com.ecommerce.notification.entity.Notification;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailProvider {
    private final JavaMailSender mailSender;

    @CircuitBreaker(name="emailProvider")
    @Retry(name="emailProvider")
    public void send(Notification n) {
        String to = extractEmail(n);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(n.getTitle());
        msg.setText(n.getBody());
        mailSender.send(msg);
    }

    private String extractEmail(Notification n){
        // TODO: real emails
        return "anvarprimov07@gmail.com";
    }
}
