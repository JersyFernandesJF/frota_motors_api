package com.example.frotamotors.domain.model;

import com.example.frotamotors.domain.enums.InspectionStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Entity
@Table(name = "inspections")
@NoArgsConstructor
public class Inspection {
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

  @ManyToOne
  @JoinColumn(name = "inspector_id")
  private User inspector;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private InspectionStatus status = InspectionStatus.PENDING;

  @Column(name = "scheduled_at")
  private LocalDateTime scheduledAt;

  @Column(name = "location", length = 500)
  private String location;

  @Column(name = "notes", columnDefinition = "TEXT")
  private String notes;

  @Column(name = "report_url", length = 500)
  private String reportUrl;

  @Column(name = "confirmed_at")
  private LocalDateTime confirmedAt;

  @Column(name = "completed_at")
  private LocalDateTime completedAt;

  @Column(name = "cancelled_at")
  private LocalDateTime cancelledAt;

  @Column(name = "cancellation_reason", columnDefinition = "TEXT")
  private String cancellationReason;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime updatedAt;
}
