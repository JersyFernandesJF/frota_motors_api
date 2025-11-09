package com.example.frotamotors.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record SharedListCreateDTO(@NotNull UUID ownerId, @NotBlank String name) {}
