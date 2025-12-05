package com.example.frotamotors.infrastructure.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.frotamotors.domain.enums.VehicleStatus;
import com.example.frotamotors.domain.enums.VehicleType;

public record VehicleSummaryDTO(
    UUID id,
    VehicleType type,
    VehicleStatus status,
    String brand,
    String model,
    Integer year,
    String color,
    Integer mileageKm,
    BigDecimal price,
    String currency,
    String fuelType,
    String transmissionType,
    Double engineSize,
    Integer horsePower,
    String thumbnailUrl,
    LocalDateTime createdAt) {}

