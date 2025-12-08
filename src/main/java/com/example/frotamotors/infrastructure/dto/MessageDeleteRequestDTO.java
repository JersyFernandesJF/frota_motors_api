package com.example.frotamotors.infrastructure.dto;

import jakarta.validation.constraints.NotNull;

public record MessageDeleteRequestDTO(@NotNull(message = "Reason is required") String reason) {}
