package com.example.frotamotors.domain.model;

import com.example.frotamotors.domain.enums.Currency;
import com.example.frotamotors.domain.enums.SubscriptionPaymentStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Entity
@Table(name = "subscription_payments")
@NoArgsConstructor
public class SubscriptionPayment {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "subscription_id", nullable = false)
  private Subscription subscription;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 3)
  private Currency currency;

  @Column(name = "payment_method", nullable = false, length = 50)
  private String paymentMethod;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_status", nullable = false, length = 20)
  private SubscriptionPaymentStatus paymentStatus;

  @Column(name = "transaction_id", length = 255)
  private String transactionId;

  @Column(name = "payment_date")
  private LocalDateTime paymentDate;

  @Column(name = "due_date", nullable = false)
  private LocalDateTime dueDate;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;
}

