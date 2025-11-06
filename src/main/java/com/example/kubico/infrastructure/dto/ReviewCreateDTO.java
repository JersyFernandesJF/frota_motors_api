package com.example.kubico.infrastructure.dto;

import com.example.kubico.domain.enums.ReviewType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ReviewCreateDTO(
    @NotNull UUID reviewerId,
    @NotNull ReviewType type,
    @NotNull @Min(1) @Max(5) Integer rating,
    String comment,
    UUID reviewedUserId,
    UUID reviewedVehicleId,
    UUID reviewedPartId,
    UUID reviewedAgencyId,
    UUID reviewedRentalId) {}

