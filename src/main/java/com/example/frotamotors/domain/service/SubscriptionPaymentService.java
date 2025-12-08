package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.model.Subscription;
import com.example.frotamotors.domain.model.SubscriptionPayment;
import com.example.frotamotors.infrastructure.dto.SubscriptionPaymentCreateDTO;
import com.example.frotamotors.infrastructure.mapper.SubscriptionPaymentMapper;
import com.example.frotamotors.infrastructure.persistence.SubscriptionPaymentRepository;
import com.example.frotamotors.infrastructure.persistence.SubscriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubscriptionPaymentService {

  @Autowired private SubscriptionPaymentRepository paymentRepository;

  @Autowired private SubscriptionRepository subscriptionRepository;

  @Transactional
  public SubscriptionPayment createPayment(SubscriptionPaymentCreateDTO dto) {
    Subscription subscription =
        subscriptionRepository
            .findById(dto.subscriptionId())
            .orElseThrow(() -> new EntityNotFoundException("Subscription not found"));

    SubscriptionPayment payment = SubscriptionPaymentMapper.toEntity(dto, subscription);
    return paymentRepository.save(payment);
  }

  public SubscriptionPayment getPaymentById(UUID id) {
    return paymentRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
  }

  public List<SubscriptionPayment> getPaymentsBySubscriptionId(UUID subscriptionId) {
    return paymentRepository.findBySubscriptionIdOrderByCreatedAtDesc(subscriptionId);
  }
}
