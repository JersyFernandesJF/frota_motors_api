package com.example.frotamotors.infrastructure.dto;

import java.util.UUID;

public record LocationResponseDTO(
    UUID id,
    String address,
    String city,
    String district,
    String postalCode,
    Double latitude,
    Double longitude) {}
