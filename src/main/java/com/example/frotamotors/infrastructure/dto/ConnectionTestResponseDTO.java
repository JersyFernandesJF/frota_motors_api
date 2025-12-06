package com.example.frotamotors.infrastructure.dto;

public record ConnectionTestResponseDTO(Boolean connected, String message, Long responseTime) {}

