package com.example.kubico.infrastructure.dto;

import com.example.kubico.domain.enums.ComplaintStatus;
import com.example.kubico.domain.enums.ComplaintType;
import com.example.kubico.domain.model.Agency;
import com.example.kubico.domain.model.Part;
import com.example.kubico.domain.model.Property;
import com.example.kubico.domain.model.User;
import com.example.kubico.domain.model.Vehicle;
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

