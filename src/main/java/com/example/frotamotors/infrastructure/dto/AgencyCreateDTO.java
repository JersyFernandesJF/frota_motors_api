package com.example.frotamotors.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record AgencyCreateDTO(
    UUID userId,
    @NotBlank @Size(max = 150) String agencyName,
    String licenseNumber,
    String logoUrl,
    String description,
    String website) {}
