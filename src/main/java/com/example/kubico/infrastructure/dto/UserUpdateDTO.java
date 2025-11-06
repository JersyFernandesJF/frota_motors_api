package com.example.kubico.infrastructure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
    @NotBlank @Size(min = 2, max = 100) String name,
    @NotBlank @Email String email,
    String imageUrl) {}

