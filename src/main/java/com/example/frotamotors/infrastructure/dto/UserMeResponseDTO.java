package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.Role;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserMeResponseDTO(
    UUID id,
    String name,
    String email,
    Role role,
    List<String> permissions,
    String avatar,
    LocalDateTime lastLogin) {}
