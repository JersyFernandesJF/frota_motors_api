package com.example.frotamotors.infrastructure.dto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record SystemConfigResponseDTO(
    UUID id,
    String key,
    Map<String, Object> value,
    String category,
    String description,
    UUID updatedById,
    String updatedByName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
