package com.example.kubico.infrastructure.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record FavoriteResponseDTO(
    UUID id,
    UUID userId,
    UUID propertyId,
    UUID vehicleId,
    UUID partId,
    LocalDateTime createdAt) {}
