package com.example.kubico.infrastructure.mapper;

import com.example.kubico.domain.enums.PaymentStatus;
import com.example.kubico.domain.model.Payment;
import com.example.kubico.domain.model.Purchase;
import com.example.kubico.domain.model.User;
import com.example.kubico.infrastructure.dto.PaymentCreateDTO;
import com.example.kubico.infrastructure.dto.PaymentResponseDTO;

public class PaymentMapper {

  private PaymentMapper() {}

  public static PaymentResponseDTO toResponse(Payment payment) {
    return new PaymentResponseDTO(
        payment.getId(),
        payment.getPurchase(),
        payment.getPayer(),
        payment.getMethod(),
        payment.getStatus(),
        payment.getAmount(),
        payment.getCurrency(),
        payment.getTransactionId(),
        payment.getPaymentReference(),
        payment.getPaymentDate(),
        payment.getNotes(),
        payment.getFailureReason(),
        payment.getCreatedAt(),
        payment.getUpdatedAt());
  }

  public static Payment toEntity(
      PaymentCreateDTO dto, Purchase purchase, User payer) {
    Payment payment = new Payment();
    payment.setPurchase(purchase);
    payment.setPayer(payer);
    payment.setMethod(dto.method());
    payment.setStatus(PaymentStatus.PENDING);
    payment.setAmount(purchase.getTotalAmount());
    payment.setCurrency(purchase.getCurrency());
    payment.setTransactionId(dto.transactionId());
    payment.setPaymentReference(dto.paymentReference());
    payment.setNotes(dto.notes());
    return payment;
  }
}

