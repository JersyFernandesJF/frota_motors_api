package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.enums.PaymentMethod;
import com.example.frotamotors.domain.enums.PaymentStatus;
import com.example.frotamotors.domain.model.Payment;
import com.example.frotamotors.domain.model.Purchase;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.infrastructure.dto.PaymentCreateDTO;
import com.example.frotamotors.infrastructure.mapper.PaymentMapper;
import com.example.frotamotors.infrastructure.persistence.PaymentRepository;
import com.example.frotamotors.infrastructure.persistence.PurchaseRepository;
import com.example.frotamotors.infrastructure.persistence.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

  @Autowired private PaymentRepository paymentRepository;

  @Autowired private PurchaseRepository purchaseRepository;

  @Autowired private UserRepository userRepository;

  @Transactional
  public Payment create(PaymentCreateDTO dto) {
    Purchase purchase =
        purchaseRepository
            .findById(dto.purchaseId())
            .orElseThrow(() -> new EntityNotFoundException("Purchase not found"));

    User payer =
        userRepository
            .findById(dto.payerId())
            .orElseThrow(() -> new EntityNotFoundException("Payer not found"));

    // Check if payment already exists for this purchase
    List<Payment> existingPayments = paymentRepository.findByPurchaseId(dto.purchaseId());
    if (!existingPayments.isEmpty()) {
      throw new IllegalStateException("Payment already exists for this purchase");
    }

    Payment payment = PaymentMapper.toEntity(dto, purchase, payer);
    return paymentRepository.save(payment);
  }

  public List<Payment> getAll() {
    return paymentRepository.findAll();
  }

  public Page<Payment> getAll(Pageable pageable) {
    return paymentRepository.findAll(pageable);
  }

  public Payment getById(UUID id) {
    return paymentRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
  }

  @Transactional
  public Payment updateStatus(
      UUID id, PaymentStatus status, String transactionId, String failureReason) {
    Payment payment = getById(id);
    payment.setStatus(status);
    if (transactionId != null) {
      payment.setTransactionId(transactionId);
    }
    if (failureReason != null) {
      payment.setFailureReason(failureReason);
    }
    if (status == PaymentStatus.COMPLETED) {
      payment.setPaymentDate(LocalDateTime.now());
    }
    Payment saved = paymentRepository.save(payment);

    // Update purchase status if payment is completed
    if (status == PaymentStatus.COMPLETED) {
      saved.getPurchase().setStatus(com.example.frotamotors.domain.enums.OrderStatus.CONFIRMED);
      purchaseRepository.save(saved.getPurchase());
    }

    return saved;
  }

  @Transactional
  public Payment processPayment(UUID id) {
    Payment payment = getById(id);
    if (payment.getStatus() != PaymentStatus.PENDING) {
      throw new IllegalStateException("Only pending payments can be processed");
    }
    payment.setStatus(PaymentStatus.PROCESSING);
    return paymentRepository.save(payment);
  }

  @Transactional
  public Payment completePayment(UUID id, String transactionId) {
    Payment payment = getById(id);
    if (payment.getStatus() != PaymentStatus.PROCESSING
        && payment.getStatus() != PaymentStatus.PENDING) {
      throw new IllegalStateException("Payment must be pending or processing to complete");
    }
    payment.setStatus(PaymentStatus.COMPLETED);
    payment.setPaymentDate(LocalDateTime.now());
    if (transactionId != null) {
      payment.setTransactionId(transactionId);
    }
    Payment saved = paymentRepository.save(payment);

    // Update purchase status
    saved.getPurchase().setStatus(com.example.frotamotors.domain.enums.OrderStatus.CONFIRMED);
    purchaseRepository.save(saved.getPurchase());

    return saved;
  }

  @Transactional
  public Payment failPayment(UUID id, String failureReason) {
    Payment payment = getById(id);
    payment.setStatus(PaymentStatus.FAILED);
    payment.setFailureReason(failureReason);
    return paymentRepository.save(payment);
  }

  @Transactional
  public Payment refundPayment(UUID id, String notes) {
    Payment payment = getById(id);
    if (payment.getStatus() != PaymentStatus.COMPLETED) {
      throw new IllegalStateException("Only completed payments can be refunded");
    }
    payment.setStatus(PaymentStatus.REFUNDED);
    if (notes != null) {
      payment.setNotes(notes);
    }
    Payment saved = paymentRepository.save(payment);

    // Update purchase status
    saved.getPurchase().setStatus(com.example.frotamotors.domain.enums.OrderStatus.REFUNDED);
    purchaseRepository.save(saved.getPurchase());

    return saved;
  }

  public void delete(UUID id) {
    if (!paymentRepository.existsById(id)) {
      throw new EntityNotFoundException("Payment not found");
    }
    paymentRepository.deleteById(id);
  }

  public List<Payment> search(UUID payerId, PaymentStatus status, PaymentMethod method) {
    return paymentRepository.search(payerId, status, method);
  }

  public Page<Payment> search(
      UUID payerId, PaymentStatus status, PaymentMethod method, Pageable pageable) {
    return paymentRepository.searchPageable(payerId, status, method, pageable);
  }

  public List<Payment> getByPayer(UUID payerId) {
    return paymentRepository.findByPayerId(payerId);
  }

  public Page<Payment> getByPayer(UUID payerId, Pageable pageable) {
    return paymentRepository.findByPayerId(payerId, pageable);
  }

  public List<Payment> getByPurchase(UUID purchaseId) {
    return paymentRepository.findByPurchaseId(purchaseId);
  }

  public Page<Payment> getByPurchase(UUID purchaseId, Pageable pageable) {
    return paymentRepository.findByPurchaseId(purchaseId, pageable);
  }

  public List<Payment> getByStatus(PaymentStatus status) {
    return paymentRepository.findByStatus(status);
  }

  public Page<Payment> getByStatus(PaymentStatus status, Pageable pageable) {
    return paymentRepository.findByStatus(status, pageable);
  }
}
