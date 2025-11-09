package com.example.frotamotors.domain.model;

import com.example.frotamotors.domain.enums.FinancingStatus;
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
@Table(name = "financings")
@NoArgsConstructor
public class Financing {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "vehicle_id", nullable = false)
  private Vehicle vehicle;

  @ManyToOne
  @JoinColumn(name = "buyer_id", nullable = false)
  private User buyer;

  @ManyToOne
  @JoinColumn(name = "seller_id", nullable = false)
  private User seller;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private FinancingStatus status = FinancingStatus.PENDING;

  @Column(nullable = false)
  private BigDecimal vehiclePrice;

  @Column(name = "down_payment", nullable = false)
  private BigDecimal downPayment;

  @Column(name = "financing_amount", nullable = false)
  private BigDecimal financingAmount;

  @Column(name = "interest_rate", nullable = false)
  private BigDecimal interestRate;

  @Column(name = "loan_term_months", nullable = false)
  private Integer loanTermMonths;

  @Column(name = "monthly_payment", nullable = false)
  private BigDecimal monthlyPayment;

  @Column(name = "credit_score")
  private Integer creditScore;

  @Column(name = "credit_score_simulation")
  private Integer creditScoreSimulation;

  @Column(name = "rejection_reason", columnDefinition = "TEXT")
  private String rejectionReason;

  @ManyToOne
  @JoinColumn(name = "approved_by_id")
  private User approvedBy;

  @Column(name = "approved_at")
  private LocalDateTime approvedAt;

  @ManyToOne
  @JoinColumn(name = "rejected_by_id")
  private User rejectedBy;

  @Column(name = "rejected_at")
  private LocalDateTime rejectedAt;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime updatedAt;
}

