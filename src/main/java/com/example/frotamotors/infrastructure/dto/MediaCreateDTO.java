package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.MediaType;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MediaCreateDTO(
    UUID vehicleId, UUID partId, @NotNull MediaType mediaType, String url) {}
