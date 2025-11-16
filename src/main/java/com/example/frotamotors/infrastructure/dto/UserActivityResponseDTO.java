package com.example.frotamotors.infrastructure.dto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record UserActivityResponseDTO(
    UUID id,
    String type,
    String description,
    String relatedEntityType,
    UUID relatedEntityId,
    Map<String, Object> metadata,
    LocalDateTime createdAt) {}
