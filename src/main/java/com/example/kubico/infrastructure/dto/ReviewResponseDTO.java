package com.example.kubico.infrastructure.dto;

import com.example.kubico.domain.enums.ReviewType;
import com.example.kubico.domain.model.Agency;
import com.example.kubico.domain.model.Part;
import com.example.kubico.domain.model.User;
import com.example.kubico.domain.model.Vehicle;
import com.example.kubico.domain.model.VehicleRental;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewResponseDTO(
    UUID id,
    User reviewer,
    ReviewType type,
    User reviewedUser,
    Vehicle reviewedVehicle,
    Part reviewedPart,
    Agency reviewedAgency,
    VehicleRental reviewedRental,
    Integer rating,
    String comment,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}

