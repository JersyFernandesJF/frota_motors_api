package com.example.frotamotors.infrastructure.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record FinancingCreateDTO(
    UUID vehicleId,
    UUID buyerId,
    UUID sellerId,
    BigDecimal vehiclePrice,
    BigDecimal downPayment,
    BigDecimal interestRate,
    Integer loanTermMonths) {}
