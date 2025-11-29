package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.ListingModerationStatus;
import com.example.frotamotors.domain.enums.VehicleStatus;
import com.example.frotamotors.domain.enums.VehicleType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record VehicleResponseDTO(
    UUID id,
    UserResponseDTO owner,
    AgencyResponseDTO agency,
    VehicleType type,
    VehicleStatus status,
    String brand,
    String model,
    Integer year,
    String color,
    String licensePlate,
    String vin,
    Integer mileageKm,
    BigDecimal price,
    String currency,
    String description,
    String fuelType,
    String transmissionType,
    Double engineSize,
    Integer horsePower,
    Integer numberOfDoors,
    Integer numberOfSeats,
    Integer previousOwners,
    Boolean accidentHistory,
    ListingModerationStatus moderationStatus,
    LocalDateTime publishedAt,
    Long views,
    Long favoritesCount,
    Long messagesCount,
    List<MediaResponseDTO> media,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
