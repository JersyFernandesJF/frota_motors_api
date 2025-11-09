package com.example.frotamotors.domain.model;

import com.example.frotamotors.domain.enums.PaymentMethod;
import com.example.frotamotors.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Entity
@Table(name = "payments")
@NoArgsConstructor
public class Payment {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "purchase_id", nullable = false)
  private Purchase purchase;

  @ManyToOne
  @JoinColumn(name = "payer_id", nullable = false)
  private User payer;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentMethod method;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentStatus status;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(nullable = false, length = 3)
  private String currency;

  @Column(name = "transaction_id", length = 255)
  private String transactionId;

  @Column(name = "payment_reference", length = 255)
  private String paymentReference;

  @Column(name = "payment_date")
  private LocalDateTime paymentDate;

  @Column(columnDefinition = "TEXT")
  private String notes;

  @Column(name = "failure_reason", columnDefinition = "TEXT")
  private String failureReason;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime updatedAt;
}

