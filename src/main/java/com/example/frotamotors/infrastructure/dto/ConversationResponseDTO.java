package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.model.User;
import java.time.LocalDateTime;
import java.util.UUID;

public record ConversationResponseDTO(
    UUID id, User user1, User user2, LocalDateTime lastMessageAt, LocalDateTime createdAt) {}
