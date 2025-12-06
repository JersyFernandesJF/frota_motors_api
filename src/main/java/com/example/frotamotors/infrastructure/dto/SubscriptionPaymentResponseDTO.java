package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.Currency;
import com.example.frotamotors.domain.enums.SubscriptionPaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record SubscriptionPaymentResponseDTO(
    UUID id,
    UUID subscriptionId,
    BigDecimal amount,
    Currency currency,
    String paymentMethod,
    SubscriptionPaymentStatus paymentStatus,
    String transactionId,
    LocalDateTime paymentDate,
    LocalDateTime dueDate,
    LocalDateTime createdAt) {}

