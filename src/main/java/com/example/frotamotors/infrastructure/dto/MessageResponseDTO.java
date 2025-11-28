package com.example.frotamotors.infrastructure.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageResponseDTO(
    UUID id,
    UUID conversationId,
    UserResponseDTO sender,
    String content,
    Boolean isRead,
    LocalDateTime createdAt) {}
