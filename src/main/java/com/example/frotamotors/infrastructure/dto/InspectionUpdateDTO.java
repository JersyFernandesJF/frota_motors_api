package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.InspectionStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record InspectionUpdateDTO(
    LocalDateTime scheduledAt,
    String location,
    String notes,
    UUID inspectorId,
    InspectionStatus status) {}
