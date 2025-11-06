package com.example.kubico.infrastructure.dto;

import com.example.kubico.infrastructure.validation.StrongPassword;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequestDTO(
    @NotBlank String token, @NotBlank @StrongPassword String newPassword) {}

