package com.example.frotamotors.infrastructure.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record FinancingApproveRequestDTO(
    @NotNull(message = "Approved amount is required") @Positive(message = "Approved amount must be positive")
        BigDecimal approvedAmount,
    @NotNull(message = "Final interest rate is required") @Positive(message = "Final interest rate must be positive")
        BigDecimal finalInterestRate,
    String notes) {}

