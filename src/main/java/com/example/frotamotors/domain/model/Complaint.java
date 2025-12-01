package com.example.frotamotors.domain.model;

import com.example.frotamotors.domain.enums.ComplaintPriority;
import com.example.frotamotors.domain.enums.ComplaintStatus;
import com.example.frotamotors.domain.enums.ComplaintType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Entity
@Table(name = "complaints")
@NoArgsConstructor
public class Complaint {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "reporter_id", nullable = false)
  private User reporter;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ComplaintType type;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ComplaintStatus status;

  @Enumerated(EnumType.STRING)
  @Column(name = "priority", length = 20)
  private ComplaintPriority priority = ComplaintPriority.MEDIUM;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String description;

  // References to the reported entity (only one should be set)
  @ManyToOne
  @JoinColumn(name = "reported_user_id")
  private User reportedUser;

  @ManyToOne
  @JoinColumn(name = "reported_vehicle_id")
  private Vehicle reportedVehicle;

  @ManyToOne
  @JoinColumn(name = "reported_part_id")
  private Part reportedPart;

  @ManyToOne
  @JoinColumn(name = "reported_agency_id")
  private Agency reportedAgency;

  @ManyToOne
  @JoinColumn(name = "reviewed_by_id")
  private User reviewedBy;

  @ManyToOne
  @JoinColumn(name = "resolved_by_id")
  private User resolvedBy;

  @Column(name = "resolved_at")
  private LocalDateTime resolvedAt;

  @ManyToOne
  @JoinColumn(name = "dismissed_by_id")
  private User dismissedBy;

  @Column(name = "dismissed_at")
  private LocalDateTime dismissedAt;

  @Column(name = "resolution", columnDefinition = "TEXT")
  private String resolution;

  @Column(columnDefinition = "TEXT")
  private String adminNotes;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime updatedAt;
}
