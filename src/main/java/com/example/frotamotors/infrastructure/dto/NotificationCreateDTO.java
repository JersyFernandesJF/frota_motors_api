package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record NotificationCreateDTO(
    @NotNull UUID userId,
    @NotNull NotificationType type,
    @NotBlank String title,
    String message,
    String relatedEntityType,
    UUID relatedEntityId,
    String actionUrl) {}

