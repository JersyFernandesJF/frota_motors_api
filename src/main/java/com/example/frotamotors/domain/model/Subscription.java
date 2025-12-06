package com.example.frotamotors.domain.model;

import com.example.frotamotors.domain.enums.Currency;
import com.example.frotamotors.domain.enums.SubscriptionPlanType;
import com.example.frotamotors.domain.enums.SubscriptionStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Entity
@Table(name = "subscriptions")
@NoArgsConstructor
public class Subscription {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "agency_id", nullable = false)
  private Agency agency;

  @Enumerated(EnumType.STRING)
  @Column(name = "plan_type", nullable = false, length = 50)
  private SubscriptionPlanType planType;

  @Column(name = "monthly_price", nullable = false, precision = 10, scale = 2)
  private BigDecimal monthlyPrice;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 3)
  private Currency currency;

  @Column(name = "max_vehicles", nullable = false)
  private Integer maxVehicles;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private SubscriptionStatus status;

  @Column(name = "start_date", nullable = false)
  private LocalDateTime startDate;

  @Column(name = "end_date")
  private LocalDateTime endDate;

  @Column(name = "next_billing_date")
  private LocalDateTime nextBillingDate;

  @Column(name = "auto_renew", nullable = false)
  private Boolean autoRenew = true;

  @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<SubscriptionPayment> payments = new ArrayList<>();

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime updatedAt;
}

