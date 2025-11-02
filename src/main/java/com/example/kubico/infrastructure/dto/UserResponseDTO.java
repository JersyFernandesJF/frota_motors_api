package com.example.kubico.infrastructure.dto;

import com.example.kubico.domain.enums.Role;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(
    UUID id,
    String name,
    String email,
    Role role,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
