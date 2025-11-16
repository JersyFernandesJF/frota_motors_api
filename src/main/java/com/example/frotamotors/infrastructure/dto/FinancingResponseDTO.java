package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.FinancingStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record FinancingResponseDTO(
    UUID id,
    UUID vehicleId,
    UUID buyerId,
    UUID sellerId,
    FinancingStatus status,
    BigDecimal vehiclePrice,
    BigDecimal downPayment,
    BigDecimal financingAmount,
    BigDecimal interestRate,
    Integer loanTermMonths,
    BigDecimal monthlyPayment,
    Integer creditScore,
    Integer creditScoreSimulation,
    String rejectionReason,
    UUID approvedById,
    LocalDateTime approvedAt,
    UUID rejectedById,
    LocalDateTime rejectedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
