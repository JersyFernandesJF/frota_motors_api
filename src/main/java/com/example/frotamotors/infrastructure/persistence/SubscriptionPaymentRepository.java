package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.model.SubscriptionPayment;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPaymentRepository extends JpaRepository<SubscriptionPayment, UUID> {
  List<SubscriptionPayment> findBySubscriptionIdOrderByCreatedAtDesc(UUID subscriptionId);
}
