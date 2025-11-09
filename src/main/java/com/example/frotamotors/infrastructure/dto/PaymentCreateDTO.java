package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PaymentCreateDTO(
    @NotNull UUID purchaseId,
    @NotNull UUID payerId,
    @NotNull PaymentMethod method,
    String transactionId,
    String paymentReference,
    String notes) {}

