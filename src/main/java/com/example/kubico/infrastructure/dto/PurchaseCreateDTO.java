package com.example.kubico.infrastructure.dto;

import com.example.kubico.domain.enums.OrderType;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PurchaseCreateDTO(
    @NotNull UUID buyerId,
    @NotNull OrderType type,
    UUID vehicleId,
    UUID partId,
    @NotNull Integer quantity,
    String shippingAddress,
    String notes) {}

