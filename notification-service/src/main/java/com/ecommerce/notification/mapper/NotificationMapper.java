package com.ecommerce.notification.mapper;

import com.ecommerce.notification.dto.NotificationDto;
import com.ecommerce.notification.entity.Notification;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationDto toNotificationDto(Notification notification);
    List<NotificationDto> toNotificationDtoList(List<Notification> notificationList);
}
