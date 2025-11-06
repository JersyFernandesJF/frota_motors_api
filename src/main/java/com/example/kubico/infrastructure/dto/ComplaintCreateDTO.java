package com.example.kubico.infrastructure.dto;

import com.example.kubico.domain.enums.ComplaintType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ComplaintCreateDTO(
    @NotNull UUID reporterId,
    @NotNull ComplaintType type,
    @NotBlank String description,
    UUID reportedUserId,
    UUID reportedVehicleId,
    UUID reportedPartId,
    UUID reportedPropertyId,
    UUID reportedAgencyId) {}

