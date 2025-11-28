package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.PropertyStatus;
import com.example.frotamotors.domain.enums.PropertyType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PropertyResponseDTO(
    UUID id,
    UserResponseDTO owner,
    AgencyResponseDTO agency,
    String title,
    String description,
    PropertyType type,
    PropertyStatus status,
    BigDecimal price,
    String currency,
    Double areaM2,
    Integer rooms,
    Integer bathrooms,
    Integer floor,
    Integer totalFloors,
    Integer yearBuilt,
    String energyCertificate,
    List<MediaResponseDTO> media,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
