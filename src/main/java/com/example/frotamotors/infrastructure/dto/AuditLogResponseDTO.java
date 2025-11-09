package com.example.frotamotors.infrastructure.dto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record AuditLogResponseDTO(
    UUID id,
    UUID userId,
    String userName,
    String action,
    String entityType,
    UUID entityId,
    String ipAddress,
    String userAgent,
    String requestMethod,
    String requestPath,
    Map<String, Object> oldValues,
    Map<String, Object> newValues,
    Integer statusCode,
    String errorMessage,
    LocalDateTime createdAt) {}

