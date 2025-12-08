package com.example.frotamotors.infrastructure.mapper;

import com.example.frotamotors.domain.model.Agency;
import com.example.frotamotors.domain.model.Subscription;
import com.example.frotamotors.infrastructure.dto.SubscriptionCreateDTO;
import com.example.frotamotors.infrastructure.dto.SubscriptionResponseDTO;
import java.time.LocalDateTime;

public class SubscriptionMapper {

  private SubscriptionMapper() {}

  public static SubscriptionResponseDTO toResponse(Subscription subscription) {
    return new SubscriptionResponseDTO(
        subscription.getId(),
        subscription.getAgency().getId(),
        subscription.getAgency().getAgencyName(),
        subscription.getPlanType(),
        subscription.getMonthlyPrice(),
        subscription.getCurrency(),
        subscription.getMaxVehicles(),
        subscription.getStatus(),
        subscription.getStartDate(),
        subscription.getEndDate(),
        subscription.getNextBillingDate(),
        subscription.getAutoRenew(),
        subscription.getCreatedAt(),
        subscription.getUpdatedAt());
  }

  public static Subscription toEntity(SubscriptionCreateDTO dto, Agency agency) {
    Subscription subscription = new Subscription();
    subscription.setAgency(agency);
    subscription.setPlanType(dto.planType());
    subscription.setMonthlyPrice(dto.monthlyPrice());
    subscription.setCurrency(dto.currency());
    subscription.setMaxVehicles(dto.maxVehicles());
    subscription.setStatus(com.example.frotamotors.domain.enums.SubscriptionStatus.PENDING);
    subscription.setStartDate(dto.startDate() != null ? dto.startDate() : LocalDateTime.now());
    subscription.setAutoRenew(dto.autoRenew() != null ? dto.autoRenew() : true);

    // Set next billing date to 1 month from start date
    if (subscription.getStartDate() != null) {
      subscription.setNextBillingDate(subscription.getStartDate().plusMonths(1));
    }

    return subscription;
  }
}
