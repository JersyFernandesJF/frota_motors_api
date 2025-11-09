package com.example.frotamotors.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record LocationCreateDTO(
    @NotNull UUID propertyId,
    @NotBlank String address,
    @NotBlank String city,
    String district,
    String postalCode,
    Double latitude,
    Double longitude) {}
