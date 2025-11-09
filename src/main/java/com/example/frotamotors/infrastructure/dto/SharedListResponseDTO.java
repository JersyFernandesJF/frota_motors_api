package com.example.frotamotors.infrastructure.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SharedListResponseDTO(UUID id, UUID ownerId, String name, LocalDateTime createdAt) {}
