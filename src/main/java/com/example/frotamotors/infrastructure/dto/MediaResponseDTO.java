package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.MediaType;
import java.time.LocalDateTime;
import java.util.UUID;

public record MediaResponseDTO(
    UUID id, MediaType mediaType, String url, LocalDateTime uploadedAt) {}
