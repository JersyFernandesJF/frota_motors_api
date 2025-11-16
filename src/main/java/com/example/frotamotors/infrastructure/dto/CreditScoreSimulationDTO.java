package com.example.frotamotors.infrastructure.dto;

import java.math.BigDecimal;

public record CreditScoreSimulationDTO(
    BigDecimal monthlyIncome,
    BigDecimal monthlyExpenses,
    Integer existingLoans,
    BigDecimal existingLoanAmount,
    Integer creditHistoryMonths) {}
