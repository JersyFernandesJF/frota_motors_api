package com.example.kubico.infrastructure.dto;

import com.example.kubico.domain.enums.NotificationType;
import com.example.kubico.domain.model.User;
import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponseDTO(
    UUID id,
    User user,
    NotificationType type,
    String title,
    String message,
    Boolean isRead,
    String relatedEntityType,
    UUID relatedEntityId,
    String actionUrl,
    LocalDateTime createdAt) {}

