package com.example.kubico.domain.model;

import com.example.kubico.domain.enums.ReviewType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Entity
@Table(name = "reviews")
@NoArgsConstructor
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "reviewer_id", nullable = false)
  private User reviewer;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReviewType type;

  // References to the reviewed entity (only one should be set)
  @ManyToOne
  @JoinColumn(name = "reviewed_user_id")
  private User reviewedUser;

  @ManyToOne
  @JoinColumn(name = "reviewed_vehicle_id")
  private Vehicle reviewedVehicle;

  @ManyToOne
  @JoinColumn(name = "reviewed_part_id")
  private Part reviewedPart;

  @ManyToOne
  @JoinColumn(name = "reviewed_agency_id")
  private Agency reviewedAgency;

  @ManyToOne
  @JoinColumn(name = "reviewed_rental_id")
  private VehicleRental reviewedRental;

  @Column(nullable = false)
  private Integer rating; // 1-5 stars

  @Column(columnDefinition = "TEXT")
  private String comment;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime updatedAt;
}

