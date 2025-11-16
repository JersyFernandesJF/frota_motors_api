package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.model.User;
import java.time.LocalDateTime;
import java.util.UUID;

public record MessageResponseDTO(
    UUID id,
    UUID conversationId,
    User sender,
    String content,
    Boolean isRead,
    LocalDateTime createdAt) {}
