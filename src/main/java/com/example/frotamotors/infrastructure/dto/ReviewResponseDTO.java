package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.ReviewType;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewResponseDTO(
    UUID id,
    UserResponseDTO reviewer,
    ReviewType type,
    UserResponseDTO reviewedUser,
    VehicleResponseDTO reviewedVehicle,
    PartResponseDTO reviewedPart,
    AgencyResponseDTO reviewedAgency,
    VehicleRentalResponseDTO reviewedRental,
    Integer rating,
    String comment,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
