package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.Role;
import java.util.List;
import java.util.UUID;

public record AuthResponseDTO(
    String token,
    String refreshToken,
    UUID userId,
    String email,
    String name,
    Role role,
    List<String> permissions) {

  // Constructor for backward compatibility (without refreshToken and permissions)
  public AuthResponseDTO(String token, UUID userId, String email, String name, Role role) {
    this(token, null, userId, email, name, role, null);
  }

  // Constructor for backward compatibility (without permissions)
  public AuthResponseDTO(
      String token, String refreshToken, UUID userId, String email, String name, Role role) {
    this(token, refreshToken, userId, email, name, role, null);
  }
}
