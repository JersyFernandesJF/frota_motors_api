package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.Currency;
import com.example.frotamotors.domain.enums.SubscriptionPlanType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record SubscriptionCreateDTO(
    @NotNull UUID agencyId,
    @NotNull SubscriptionPlanType planType,
    @NotNull @Positive BigDecimal monthlyPrice,
    @NotNull Currency currency,
    @NotNull @Positive Integer maxVehicles,
    LocalDateTime startDate,
    Boolean autoRenew) {}

