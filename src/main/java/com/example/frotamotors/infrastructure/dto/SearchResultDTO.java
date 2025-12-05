package com.example.frotamotors.infrastructure.dto;

import java.util.List;

public record SearchResultDTO(
    List<VehicleResponseDTO> vehicles,
    List<PartResponseDTO> parts,
    Long totalVehicles,
    Long totalParts) {}
