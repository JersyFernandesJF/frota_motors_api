package com.example.frotamotors.infrastructure.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record VehicleBulkRejectRequestDTO(
    @NotEmpty(message = "Vehicle IDs list cannot be empty") List<UUID> vehicleIds,
    @NotNull(message = "Reason is required") String reason) {}
