package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.enums.SubscriptionStatus;
import com.example.frotamotors.domain.model.Agency;
import com.example.frotamotors.domain.model.Subscription;
import com.example.frotamotors.infrastructure.dto.SubscriptionCreateDTO;
import com.example.frotamotors.infrastructure.mapper.SubscriptionMapper;
import com.example.frotamotors.infrastructure.persistence.AgencyRepository;
import com.example.frotamotors.infrastructure.persistence.SubscriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubscriptionService {

  @Autowired private SubscriptionRepository subscriptionRepository;

  @Autowired private AgencyRepository agencyRepository;

  @Transactional
  public Subscription createSubscription(SubscriptionCreateDTO dto) {
    Agency agency =
        agencyRepository
            .findById(dto.agencyId())
            .orElseThrow(() -> new EntityNotFoundException("Agency not found"));

    // Check if agency already has an active subscription
    subscriptionRepository
        .findByAgencyIdAndStatus(dto.agencyId(), SubscriptionStatus.ACTIVE)
        .ifPresent(
            existing -> {
              throw new IllegalStateException(
                  "Agency already has an active subscription");
            });

    Subscription subscription = SubscriptionMapper.toEntity(dto, agency);
    subscription.setStatus(SubscriptionStatus.ACTIVE);
    
    // Set end date if provided, otherwise calculate from start date
    if (dto.startDate() != null) {
      subscription.setEndDate(dto.startDate().plusMonths(1));
    } else {
      subscription.setEndDate(LocalDateTime.now().plusMonths(1));
    }

    Subscription saved = subscriptionRepository.save(subscription);
    
    // Update agency with subscription reference and activate it
    agency.setSubscription(saved);
    agency.setIsActive(true);
    agencyRepository.save(agency);

    return saved;
  }

  public Subscription getSubscriptionById(UUID id) {
    return subscriptionRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Subscription not found"));
  }

  public Subscription getSubscriptionByAgencyId(UUID agencyId) {
    return subscriptionRepository
        .findByAgencyId(agencyId)
        .orElseThrow(() -> new EntityNotFoundException("Subscription not found for agency"));
  }

  public Subscription getActiveSubscriptionByAgencyId(UUID agencyId) {
    return subscriptionRepository
        .findByAgencyIdAndStatus(agencyId, SubscriptionStatus.ACTIVE)
        .orElseThrow(
            () ->
                new EntityNotFoundException(
                    "Active subscription not found for agency"));
  }

  @Transactional
  public Subscription renewSubscription(UUID subscriptionId) {
    Subscription subscription = getSubscriptionById(subscriptionId);
    
    if (subscription.getStatus() != SubscriptionStatus.ACTIVE
        && subscription.getStatus() != SubscriptionStatus.EXPIRED) {
      throw new IllegalStateException(
          "Only active or expired subscriptions can be renewed");
    }

    subscription.setStatus(SubscriptionStatus.ACTIVE);
    subscription.setStartDate(LocalDateTime.now());
    subscription.setEndDate(LocalDateTime.now().plusMonths(1));
    subscription.setNextBillingDate(LocalDateTime.now().plusMonths(1));

    return subscriptionRepository.save(subscription);
  }

  @Transactional
  public Subscription cancelSubscription(UUID subscriptionId) {
    Subscription subscription = getSubscriptionById(subscriptionId);
    subscription.setStatus(SubscriptionStatus.CANCELLED);
    subscription.setAutoRenew(false);
    
    Agency agency = subscription.getAgency();
    agency.setIsActive(false);
    agencyRepository.save(agency);

    return subscriptionRepository.save(subscription);
  }

  @Transactional
  public Subscription suspendSubscription(UUID subscriptionId) {
    Subscription subscription = getSubscriptionById(subscriptionId);
    subscription.setStatus(SubscriptionStatus.SUSPENDED);
    
    Agency agency = subscription.getAgency();
    agency.setIsActive(false);
    agencyRepository.save(agency);

    return subscriptionRepository.save(subscription);
  }

  public boolean canCreateVehicle(UUID agencyId) {
    try {
      Subscription subscription = getActiveSubscriptionByAgencyId(agencyId);
      Agency agency = subscription.getAgency();
      
      return agency.getCurrentVehicleCount() < subscription.getMaxVehicles();
    } catch (EntityNotFoundException e) {
      return false;
    }
  }

  public void incrementVehicleCount(UUID agencyId) {
    Agency agency =
        agencyRepository
            .findById(agencyId)
            .orElseThrow(() -> new EntityNotFoundException("Agency not found"));
    
    agency.setCurrentVehicleCount(agency.getCurrentVehicleCount() + 1);
    agencyRepository.save(agency);
  }

  public void decrementVehicleCount(UUID agencyId) {
    Agency agency =
        agencyRepository
            .findById(agencyId)
            .orElseThrow(() -> new EntityNotFoundException("Agency not found"));
    
    int currentCount = agency.getCurrentVehicleCount();
    if (currentCount > 0) {
      agency.setCurrentVehicleCount(currentCount - 1);
      agencyRepository.save(agency);
    }
  }

  public List<Subscription> getAllSubscriptions() {
    return subscriptionRepository.findAll();
  }
}

