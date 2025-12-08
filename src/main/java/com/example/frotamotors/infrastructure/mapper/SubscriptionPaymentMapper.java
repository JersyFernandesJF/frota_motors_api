package com.example.frotamotors.infrastructure.mapper;

import com.example.frotamotors.domain.model.Subscription;
import com.example.frotamotors.domain.model.SubscriptionPayment;
import com.example.frotamotors.infrastructure.dto.SubscriptionPaymentCreateDTO;
import com.example.frotamotors.infrastructure.dto.SubscriptionPaymentResponseDTO;

public class SubscriptionPaymentMapper {

  private SubscriptionPaymentMapper() {}

  public static SubscriptionPaymentResponseDTO toResponse(SubscriptionPayment payment) {
    return new SubscriptionPaymentResponseDTO(
        payment.getId(),
        payment.getSubscription().getId(),
        payment.getAmount(),
        payment.getCurrency(),
        payment.getPaymentMethod(),
        payment.getPaymentStatus(),
        payment.getTransactionId(),
        payment.getPaymentDate(),
        payment.getDueDate(),
        payment.getCreatedAt());
  }

  public static SubscriptionPayment toEntity(
      SubscriptionPaymentCreateDTO dto, Subscription subscription) {
    SubscriptionPayment payment = new SubscriptionPayment();
    payment.setSubscription(subscription);
    payment.setAmount(dto.amount());
    payment.setCurrency(dto.currency());
    payment.setPaymentMethod(dto.paymentMethod());
    payment.setPaymentStatus(dto.paymentStatus());
    payment.setTransactionId(dto.transactionId());
    payment.setPaymentDate(dto.paymentDate());
    payment.setDueDate(dto.dueDate());
    return payment;
  }
}
