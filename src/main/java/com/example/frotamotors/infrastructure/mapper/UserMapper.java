package com.example.frotamotors.infrastructure.mapper;

import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.infrastructure.dto.UserCreateDTO;
import com.example.frotamotors.infrastructure.dto.UserResponseDTO;

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
