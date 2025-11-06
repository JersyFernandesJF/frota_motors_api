package com.example.kubico.infrastructure.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record FavoriteCreateDTO(
    @NotNull UUID userId,
    UUID propertyId,
    UUID vehicleId,
    UUID partId) {}
