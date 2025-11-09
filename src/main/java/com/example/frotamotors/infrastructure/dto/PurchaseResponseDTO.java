package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.OrderStatus;
import com.example.frotamotors.domain.enums.OrderType;
import com.example.frotamotors.domain.model.Part;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.Vehicle;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PurchaseResponseDTO(
    UUID id,
    User buyer,
    OrderType type,
    OrderStatus status,
    Vehicle vehicle,
    Part part,
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

