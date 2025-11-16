package com.example.frotamotors.infrastructure.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record InspectionCreateDTO(
    UUID vehicleId,
    UUID buyerId,
    UUID sellerId,
    LocalDateTime scheduledAt,
    String location,
    String notes) {}
