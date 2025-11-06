package com.example.kubico.infrastructure.dto;

import com.example.kubico.domain.model.User;
import java.time.LocalDateTime;
import java.util.UUID;

public record MessageResponseDTO(
    UUID id,
    UUID conversationId,
    User sender,
    String content,
    Boolean isRead,
    LocalDateTime createdAt) {}

