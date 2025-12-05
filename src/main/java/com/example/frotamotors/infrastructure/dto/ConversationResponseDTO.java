package com.example.frotamotors.infrastructure.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConversationResponseDTO(
    UUID id,
    UserResponseDTO user1,
    UserResponseDTO user2,
    LocalDateTime lastMessageAt,
    LocalDateTime createdAt) {}
