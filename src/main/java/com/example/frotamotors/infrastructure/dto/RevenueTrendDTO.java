package com.example.frotamotors.infrastructure.dto;

import java.math.BigDecimal;

public record RevenueTrendDTO(String date, BigDecimal revenue, BigDecimal projectedRevenue) {}
