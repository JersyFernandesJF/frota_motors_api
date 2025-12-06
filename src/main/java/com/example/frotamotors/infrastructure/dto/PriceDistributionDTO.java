package com.example.frotamotors.infrastructure.dto;

import java.math.BigDecimal;

public record PriceDistributionDTO(String range, Long count, BigDecimal minPrice, BigDecimal maxPrice) {}

