package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.PropertyStatus;
import com.example.frotamotors.domain.enums.PropertyType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PropertySummaryDTO(
    UUID id,
    String title,
    PropertyType type,
    PropertyStatus status,
    BigDecimal price,
    String currency,
    Double areaM2,
    Integer rooms,
    Integer bathrooms,
    Integer yearBuilt,
    String thumbnailUrl,
    LocalDateTime createdAt) {}

