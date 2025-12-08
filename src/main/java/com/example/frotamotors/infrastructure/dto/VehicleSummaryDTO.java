package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.VehicleStatus;
import com.example.frotamotors.domain.enums.VehicleType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record VehicleSummaryDTO(
    UUID id,
    UUID ownerId, // Nullable - pode ser null se não houver owner
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
