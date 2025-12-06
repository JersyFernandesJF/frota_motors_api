package com.example.frotamotors.infrastructure.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

public record VehicleBulkApproveRequestDTO(
    @NotEmpty(message = "Vehicle IDs list cannot be empty") List<UUID> vehicleIds,
    String notes) {}

