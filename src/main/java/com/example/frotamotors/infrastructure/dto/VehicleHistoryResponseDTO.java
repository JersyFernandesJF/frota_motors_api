package com.example.frotamotors.infrastructure.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record VehicleHistoryResponseDTO(
    UUID id,
    String action,
    String oldValue,
    String newValue,
    UUID changedById,
    String changedByName,
    LocalDateTime changedAt) {}

