package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.InspectionStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record InspectionResponseDTO(
    UUID id,
    VehicleResponseDTO vehicle,
    UserResponseDTO buyer,
    UserResponseDTO seller,
    UserResponseDTO inspector,
    InspectionStatus status,
    LocalDateTime scheduledAt,
    String location,
    String notes,
    String reportUrl,
    LocalDateTime confirmedAt,
    LocalDateTime completedAt,
    LocalDateTime cancelledAt,
    String cancellationReason,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
