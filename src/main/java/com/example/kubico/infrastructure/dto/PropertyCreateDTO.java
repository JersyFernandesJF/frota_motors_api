package com.example.kubico.infrastructure.dto;

import com.example.kubico.domain.enums.PropertyStatus;
import com.example.kubico.domain.enums.PropertyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PropertyCreateDTO(
    @NotNull UUID ownerId,
    UUID agencyId,
    @NotBlank String title,
    String description,
    @NotNull PropertyType type,
    @NotNull PropertyStatus status,
    @NotNull Double price,
    @NotBlank String currency,
    Double areaM2,
    Integer rooms,
    Integer bathrooms,
    Integer floor,
    Integer totalFloors,
    Integer yearBuilt,
    String energyCertificate) {}
