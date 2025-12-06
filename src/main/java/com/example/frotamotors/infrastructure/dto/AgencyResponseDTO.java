package com.example.frotamotors.infrastructure.dto;

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
    UUID subscriptionId,
    Boolean isActive,
    Integer currentVehicleCount,
    String phone,
    String address,
    String taxId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
