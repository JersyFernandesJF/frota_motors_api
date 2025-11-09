package com.example.frotamotors.infrastructure.mapper;

import com.example.frotamotors.domain.model.Alerts;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.infrastructure.dto.NotificationCreateDTO;
import com.example.frotamotors.infrastructure.dto.NotificationResponseDTO;

public class NotificationMapper {

  private NotificationMapper() {}

  public static NotificationResponseDTO toResponse(Alerts notification) {
    return new NotificationResponseDTO(
        notification.getId(),
        notification.getUser(),
        notification.getType(),
        notification.getTitle(),
        notification.getMessage(),
        notification.getIsRead(),
        notification.getRelatedEntityType(),
        notification.getRelatedEntityId(),
        notification.getActionUrl(),
        notification.getCreatedAt());
  }

  public static Alerts toEntity(NotificationCreateDTO dto, User user) {
    Alerts notification = new Alerts();
    notification.setUser(user);
    notification.setType(dto.type());
    notification.setTitle(dto.title());
    notification.setMessage(dto.message());
    notification.setIsRead(false);
    notification.setRelatedEntityType(dto.relatedEntityType());
    notification.setRelatedEntityId(dto.relatedEntityId());
    notification.setActionUrl(dto.actionUrl());
    return notification;
  }
}

