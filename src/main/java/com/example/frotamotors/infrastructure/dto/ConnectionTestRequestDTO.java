package com.example.frotamotors.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;

public record ConnectionTestRequestDTO(
    @NotBlank(message = "API key is required") String apiKey,
    @NotBlank(message = "Service name is required") String service) {}
