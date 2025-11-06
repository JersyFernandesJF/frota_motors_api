package com.example.kubico.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;

public record AppleAuthRequestDTO(@NotBlank String idToken) {}

