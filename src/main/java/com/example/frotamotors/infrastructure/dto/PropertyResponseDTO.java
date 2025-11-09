package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.PropertyStatus;
import com.example.frotamotors.domain.enums.PropertyType;
import com.example.frotamotors.domain.model.Agency;
import com.example.frotamotors.domain.model.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PropertyResponseDTO(
    UUID id,
    User owner,
    Agency agency,
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
