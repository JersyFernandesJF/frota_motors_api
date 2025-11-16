package com.example.frotamotors.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;

public record GoogleAuthRequestDTO(@NotBlank String idToken) {}
