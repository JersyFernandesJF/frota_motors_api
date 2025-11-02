package com.example.kubico.infrastructure.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record SharedListItemCreateDTO(@NotNull UUID listId, @NotNull UUID propertyId) {}
