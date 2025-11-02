package com.example.kubico.infrastructure.dto;

import com.example.kubico.domain.enums.MediaType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MediaCreateDTO(
    @NotNull UUID propertyId, @NotNull MediaType mediaType, @NotBlank String url) {}
