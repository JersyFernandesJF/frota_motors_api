package com.example.kubico.infrastructure.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AgencyResponseDTO(
    UUID id,
    String agencyName,
    String email,
    String licenseNumber,
    String logoUrl,
    String description,
    String website,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
