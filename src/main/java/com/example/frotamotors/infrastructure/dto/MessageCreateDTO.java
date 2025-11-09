package com.example.frotamotors.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MessageCreateDTO(
    @NotNull UUID conversationId, @NotNull UUID senderId, @NotBlank String content) {}

