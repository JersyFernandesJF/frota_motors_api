package com.example.kubico.infrastructure.dto;

import com.example.kubico.domain.enums.PartCategory;
import com.example.kubico.domain.enums.PartStatus;
import com.example.kubico.domain.model.Agency;
import com.example.kubico.domain.model.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PartResponseDTO(
    UUID id,
    User seller,
    Agency agency,
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

