package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.PartCategory;
import com.example.frotamotors.domain.enums.PartStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PartResponseDTO(
    UUID id,
    UserResponseDTO seller,
    AgencyResponseDTO agency,
    PartCategory category,
    PartStatus status,
    String name,
    String description,
    BigDecimal price,
    String currency,
    String partNumber,
    String oemNumber,
    String brand,
    String compatibleVehicles,
    String conditionType,
    Integer quantityAvailable,
    Integer warrantyMonths,
    List<MediaResponseDTO> media,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
