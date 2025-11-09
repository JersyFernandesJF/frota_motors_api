package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.PartCategory;
import com.example.frotamotors.domain.enums.PartStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PartCreateDTO(
    @NotNull UUID sellerId,
    UUID agencyId,
    @NotNull PartCategory category,
    @NotNull PartStatus status,
    @NotBlank String name,
    String description,
    @NotNull Double price,
    @NotBlank String currency,
    String partNumber,
    String oemNumber,
    String brand,
    String compatibleVehicles,
    String conditionType,
    Integer quantityAvailable,
    Integer warrantyMonths) {}

