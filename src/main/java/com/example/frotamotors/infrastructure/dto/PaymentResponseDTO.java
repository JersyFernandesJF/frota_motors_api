package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.PaymentMethod;
import com.example.frotamotors.domain.enums.PaymentStatus;
import com.example.frotamotors.domain.model.Purchase;
import com.example.frotamotors.domain.model.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponseDTO(
    UUID id,
    Purchase purchase,
    User payer,
    PaymentMethod method,
    PaymentStatus status,
    BigDecimal amount,
    String currency,
    String transactionId,
    String paymentReference,
    LocalDateTime paymentDate,
    String notes,
    String failureReason,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
