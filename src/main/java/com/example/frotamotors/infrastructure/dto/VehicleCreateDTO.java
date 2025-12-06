package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.Currency;
import com.example.frotamotors.domain.enums.VehicleStatus;
import com.example.frotamotors.domain.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record VehicleCreateDTO(
    @NotNull UUID ownerId,
    UUID agencyId,
    @NotNull VehicleType type,
    @NotNull VehicleStatus status,
    @NotBlank String brand,
    @NotBlank String model,
    @NotNull Integer year,
    String color,
    String licensePlate,
    String vin,
    Integer mileageKm,
    @NotNull Double price,
    @NotNull Currency currency,
    String description,
    String fuelType,
    String transmissionType,
    Double engineSize,
    Integer horsePower,
    Integer numberOfDoors,
    Integer numberOfSeats,
    Integer previousOwners,
    Boolean accidentHistory) {}
