package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.OrderStatus;
import com.example.frotamotors.domain.enums.OrderType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PurchaseResponseDTO(
    UUID id,
    UserResponseDTO buyer,
    OrderType type,
    OrderStatus status,
    VehicleResponseDTO vehicle,
    PartResponseDTO part,
    BigDecimal price,
    String currency,
    Integer quantity,
    BigDecimal totalAmount,
    String shippingAddress,
    LocalDateTime deliveryDate,
    String trackingNumber,
    String notes,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
