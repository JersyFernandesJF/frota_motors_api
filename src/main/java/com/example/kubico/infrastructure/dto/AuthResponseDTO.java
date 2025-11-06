package com.example.kubico.infrastructure.dto;

import com.example.kubico.domain.enums.Role;
import java.util.UUID;

public record AuthResponseDTO(
    String token,
    String refreshToken,
    UUID userId,
    String email,
    String name,
    Role role) {

  // Constructor for backward compatibility (without refreshToken)
  public AuthResponseDTO(String token, UUID userId, String email, String name, Role role) {
    this(token, null, userId, email, name, role);
  }
}

