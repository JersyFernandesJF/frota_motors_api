package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.infrastructure.validation.StrongPassword;
import jakarta.validation.constraints.NotBlank;

public record AdminResetPasswordRequestDTO(
    @NotBlank(message = "New password is required") @StrongPassword String newPassword) {}

