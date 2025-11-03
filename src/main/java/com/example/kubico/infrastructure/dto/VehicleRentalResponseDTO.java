package com.example.kubico.infrastructure.dto;

import com.example.kubico.domain.enums.RentalStatus;
import com.example.kubico.domain.model.Agency;
import com.example.kubico.domain.model.User;
import com.example.kubico.domain.model.Vehicle;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record VehicleRentalResponseDTO(
    UUID id,
    Vehicle vehicle,
    User renter,
    Agency agency,
    RentalStatus status,
    LocalDate startDate,
    LocalDate endDate,
    LocalDate actualReturnDate,
    BigDecimal dailyRate,
    String currency,
    BigDecimal totalAmount,
    BigDecimal depositAmount,
    Boolean depositReturned,
    String notes,
    String pickupLocation,
    String returnLocation,
    Integer mileageAtPickup,
    Integer mileageAtReturn,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}

