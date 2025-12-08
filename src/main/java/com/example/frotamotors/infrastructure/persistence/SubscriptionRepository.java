package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.enums.SubscriptionStatus;
import com.example.frotamotors.domain.model.Subscription;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
  Optional<Subscription> findByAgencyId(UUID agencyId);

  Optional<Subscription> findByAgencyIdAndStatus(UUID agencyId, SubscriptionStatus status);
}
