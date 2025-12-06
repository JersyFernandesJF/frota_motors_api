package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.Currency;
import com.example.frotamotors.domain.enums.SubscriptionPlanType;
import com.example.frotamotors.domain.enums.SubscriptionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record SubscriptionResponseDTO(
    UUID id,
    UUID agencyId,
    String agencyName,
    SubscriptionPlanType planType,
    BigDecimal monthlyPrice,
    Currency currency,
    Integer maxVehicles,
    SubscriptionStatus status,
    LocalDateTime startDate,
    LocalDateTime endDate,
    LocalDateTime nextBillingDate,
    Boolean autoRenew,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}

