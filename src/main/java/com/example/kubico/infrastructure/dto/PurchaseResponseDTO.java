package com.example.kubico.infrastructure.dto;

import com.example.kubico.domain.enums.OrderStatus;
import com.example.kubico.domain.enums.OrderType;
import com.example.kubico.domain.model.Part;
import com.example.kubico.domain.model.User;
import com.example.kubico.domain.model.Vehicle;
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

