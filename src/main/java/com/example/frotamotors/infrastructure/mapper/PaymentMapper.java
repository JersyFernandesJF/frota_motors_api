package com.example.frotamotors.infrastructure.mapper;

import com.example.frotamotors.domain.enums.PaymentStatus;
import com.example.frotamotors.domain.model.Payment;
import com.example.frotamotors.domain.model.Purchase;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.infrastructure.dto.PaymentCreateDTO;
import com.example.frotamotors.infrastructure.dto.PaymentResponseDTO;

public class PaymentMapper {

  private PaymentMapper() {}

  public static PaymentResponseDTO toResponse(Payment payment) {
    return new PaymentResponseDTO(
        payment.getId(),
        payment.getPurchase() != null ? PurchaseMapper.toResponse(payment.getPurchase()) : null,
        payment.getPayer() != null ? UserMapper.toResponse(payment.getPayer()) : null,
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

  public static Payment toEntity(PaymentCreateDTO dto, Purchase purchase, User payer) {
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
