package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.ComplaintPriority;
import com.example.frotamotors.domain.enums.ComplaintStatus;
import com.example.frotamotors.domain.enums.ComplaintType;
import java.time.LocalDateTime;
import java.util.UUID;

public record ComplaintResponseDTO(
    UUID id,
    UserResponseDTO reporter,
    ComplaintType type,
    ComplaintStatus status,
    ComplaintPriority priority,
    String description,
    UserResponseDTO reportedUser,
    VehicleResponseDTO reportedVehicle,
    PartResponseDTO reportedPart,
    AgencyResponseDTO reportedAgency,
    UserResponseDTO reviewedBy,
    UserResponseDTO resolvedBy,
    UserResponseDTO dismissedBy,
    String adminNotes,
    String resolution,
    LocalDateTime resolvedAt,
    LocalDateTime dismissedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
