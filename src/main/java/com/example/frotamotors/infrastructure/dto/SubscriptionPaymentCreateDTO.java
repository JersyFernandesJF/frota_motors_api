package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.Currency;
import com.example.frotamotors.domain.enums.SubscriptionPaymentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record SubscriptionPaymentCreateDTO(
    @NotNull UUID subscriptionId,
    @NotNull @Positive BigDecimal amount,
    @NotNull Currency currency,
    @NotNull String paymentMethod,
    @NotNull SubscriptionPaymentStatus paymentStatus,
    String transactionId,
    LocalDateTime paymentDate,
    @NotNull LocalDateTime dueDate) {}
