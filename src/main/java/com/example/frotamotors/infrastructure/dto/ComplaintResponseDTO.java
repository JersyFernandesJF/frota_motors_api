package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.ComplaintStatus;
import com.example.frotamotors.domain.enums.ComplaintType;
import java.time.LocalDateTime;
import java.util.UUID;

public record ComplaintResponseDTO(
    UUID id,
    UserResponseDTO reporter,
    ComplaintType type,
    ComplaintStatus status,
    String description,
    UserResponseDTO reportedUser,
    VehicleResponseDTO reportedVehicle,
    PartResponseDTO reportedPart,
    PropertyResponseDTO reportedProperty,
    AgencyResponseDTO reportedAgency,
    UserResponseDTO reviewedBy,
    String adminNotes,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
