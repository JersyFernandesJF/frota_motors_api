package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.NotificationType;
import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponseDTO(
    UUID id,
    UserResponseDTO user,
    NotificationType type,
    String title,
    String message,
    Boolean isRead,
    String relatedEntityType,
    UUID relatedEntityId,
    String actionUrl,
    LocalDateTime createdAt) {}
