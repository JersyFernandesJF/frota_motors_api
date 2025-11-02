package com.example.kubico.infrastructure.dto;

import com.example.kubico.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(
    @NotBlank @Size(max = 100) String name,
    @NotBlank @Email @Size(max = 150) String email,
    @NotBlank @Size(min = 8) String password,
    Role role) {}
