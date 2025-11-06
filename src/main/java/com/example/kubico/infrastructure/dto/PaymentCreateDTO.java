package com.example.kubico.infrastructure.dto;

import com.example.kubico.domain.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PaymentCreateDTO(
    @NotNull UUID purchaseId,
    @NotNull UUID payerId,
    @NotNull PaymentMethod method,
    String transactionId,
    String paymentReference,
    String notes) {}

