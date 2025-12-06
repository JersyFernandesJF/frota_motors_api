package com.example.frotamotors.infrastructure.dto;

import jakarta.validation.constraints.NotNull;

public record UserWarnRequestDTO(
    @NotNull(message = "Message is required") String message,
    String reason) {}

