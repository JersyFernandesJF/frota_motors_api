package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.ReviewType;
import com.example.frotamotors.domain.model.Agency;
import com.example.frotamotors.domain.model.Part;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.domain.model.VehicleRental;
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
