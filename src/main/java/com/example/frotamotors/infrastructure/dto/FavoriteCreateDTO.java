package com.example.frotamotors.infrastructure.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record FavoriteCreateDTO(
    @NotNull UUID userId, UUID vehicleId, UUID partId) {}
