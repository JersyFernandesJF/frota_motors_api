package com.example.kubico.infrastructure.mapper;

import com.example.kubico.domain.model.User;
import com.example.kubico.infrastructure.dto.UserCreateDTO;
import com.example.kubico.infrastructure.dto.UserResponseDTO;

public class UserMapper {

  private UserMapper() {}

  public static UserResponseDTO toResponse(User user) {
    return new UserResponseDTO(
        user.getId(),
        user.getName(),
        user.getEmail(),
        user.getRole(),
        user.getCreatedAt(),
        user.getUpdatedAt());
  }

  public static User toEntity(UserCreateDTO dto, String encodedPassword) {
    User user = new User();
    user.setName(dto.name());
    user.setEmail(dto.email());
    user.setPasswordHash(encodedPassword);
    user.setRole(dto.role());
    return user;
  }
}
