package com.ecommerce.notification.service;

import com.ecommerce.notification.dto.*;
import com.ecommerce.notification.entity.Notification;
import com.ecommerce.notification.enums.Channel;
import com.ecommerce.notification.enums.Status;
import com.ecommerce.notification.mapper.NotificationMapper;
import com.ecommerce.notification.repository.NotificationRepository;
import com.ecommerce.notification.service.channel.EmailProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final EmailProvider emailProvider;

    @Transactional
    public Response<Object> send(SendRequest req) {
        Notification notification = Notification.builder()
                .userId(req.getUserId())
                .title(req.getTitle())
                .body(req.getBody())
                .channel(req.getChannel())
                .status(Status.PENDING)
                .build();
        deliver(notification);
        return Response.ok();
    }

    @Transactional
    public void handleEvent(NotificationEvent event) {
        Notification notification = Notification.builder()
                .userId(event.getUserId())
                .title(event.getTitle())
                .body(event.getBody())
                .channel(parseChannel(event.getChannel()))
                .status(Status.PENDING)
                .build();
        deliver(notification);
    }

    private void deliver(Notification notification) {
        if (notification.getChannel().equals(Channel.EMAIL)){
            try {
                emailProvider.send(notification);
                notification.setStatus(Status.SENT);
                notification.setSentAt(OffsetDateTime.now());
            } catch (Exception e) {
                notification.setStatus(Status.FAILED);
                notification.setSentAt(OffsetDateTime.now());
            }
        } else {
            notification.setStatus(Status.SENT);
            notification.setSentAt(OffsetDateTime.now());
        }
        notificationRepository.save(notification);
    }

    private Channel parseChannel(String s) {
        try { return s==null? Channel.EMAIL : Channel.valueOf(s); }
        catch (Exception ignored) { return Channel.EMAIL; }
    }


    public PageResponse getAll(String userId, int page, int size) {
        log.info(userId);
        Pageable pageable = PageRequest.of(page, size);
        return toPageResponse(notificationRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable));
    }

    private PageResponse toPageResponse(Page<Notification> page){
        return new PageResponse(
                notificationMapper.toNotificationDtoList(page.stream().toList()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Transactional
    public void markRead(long id, String userId) {
        Notification notification = notificationRepository.findById(id).orElseThrow();
        if (!notification.getUserId().equals(userId)) throw new RuntimeException("Forbidden");
        notification.setStatus(Status.READ);
        notification.setReadAt(OffsetDateTime.now());
        notificationRepository.save(notification);
    }
}
