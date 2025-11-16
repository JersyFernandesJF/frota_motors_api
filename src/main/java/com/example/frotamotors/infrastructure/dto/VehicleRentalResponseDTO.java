package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.RentalStatus;
import com.example.frotamotors.domain.model.Agency;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.Vehicle;
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
