package com.example.frotamotors.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Entity
@Table(name = "vehicle_history")
@NoArgsConstructor
public class VehicleHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "vehicle_id", nullable = false)
  private Vehicle vehicle;

  @Column(nullable = false, length = 50)
  private String action; // price_changed, approved, rejected, status_changed, etc.

  @Column(name = "old_value", columnDefinition = "TEXT")
  private String oldValue;

  @Column(name = "new_value", columnDefinition = "TEXT")
  private String newValue;

  @ManyToOne
  @JoinColumn(name = "changed_by_id")
  private User changedBy;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime changedAt;
}
