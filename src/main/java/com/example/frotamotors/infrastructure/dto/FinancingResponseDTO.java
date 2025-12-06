package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.FinancingStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record FinancingResponseDTO(
    UUID id,
    VehicleResponseDTO vehicle,
    UserResponseDTO buyer,
    UserResponseDTO seller,
    FinancingStatus status,
    BigDecimal vehiclePrice,
    BigDecimal requestedAmount,
    BigDecimal approvedAmount,
    BigDecimal downPayment,
    BigDecimal financingAmount,
    BigDecimal interestRate,
    Integer loanTermMonths,
    BigDecimal monthlyPayment,
    Integer creditScore,
    Integer creditScoreSimulation,
    String rejectionReason,
    String notes,
    UserResponseDTO approvedBy,
    LocalDateTime approvedAt,
    UserResponseDTO rejectedBy,
    LocalDateTime rejectedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
