package com.example.kubico.infrastructure.dto;

import com.example.kubico.domain.enums.PropertyStatus;
import com.example.kubico.domain.enums.PropertyType;
import com.example.kubico.domain.model.Agency;
import com.example.kubico.domain.model.User;
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
