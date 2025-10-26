package com.ecommerce.notification.repository;

import com.ecommerce.notification.entity.Notification;
import com.ecommerce.notification.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findAllByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);
}
