package com.example.kubico.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record VehicleRentalCreateDTO(
    @NotNull UUID vehicleId,
    @NotNull UUID renterId,
    UUID agencyId,
    @NotNull LocalDate startDate,
    @NotNull LocalDate endDate,
    @NotNull Double dailyRate,
    @NotBlank String currency,
    Double depositAmount,
    String notes,
    String pickupLocation,
    String returnLocation) {}

