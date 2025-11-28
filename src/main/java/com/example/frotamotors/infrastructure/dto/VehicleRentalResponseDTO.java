package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.RentalStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record VehicleRentalResponseDTO(
    UUID id,
    VehicleResponseDTO vehicle,
    UserResponseDTO renter,
    AgencyResponseDTO agency,
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
