package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.ComplaintStatus;
import com.example.frotamotors.domain.enums.ComplaintType;
import com.example.frotamotors.domain.model.Agency;
import com.example.frotamotors.domain.model.Part;
import com.example.frotamotors.domain.model.Property;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.Vehicle;
import java.time.LocalDateTime;
import java.util.UUID;

public record ComplaintResponseDTO(
    UUID id,
    User reporter,
    ComplaintType type,
    ComplaintStatus status,
    String description,
    User reportedUser,
    Vehicle reportedVehicle,
    Part reportedPart,
    Property reportedProperty,
    Agency reportedAgency,
    User reviewedBy,
    String adminNotes,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}

