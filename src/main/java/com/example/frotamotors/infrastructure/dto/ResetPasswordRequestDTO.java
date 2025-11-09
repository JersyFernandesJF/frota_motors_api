package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.infrastructure.validation.StrongPassword;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequestDTO(
    @NotBlank String token, @NotBlank @StrongPassword String newPassword) {}

