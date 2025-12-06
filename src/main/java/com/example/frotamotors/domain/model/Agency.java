package com.example.frotamotors.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Entity
@Table(name = "agencies")
@NoArgsConstructor
public class Agency {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "agency_name", nullable = false)
  private String agencyName;

  @Column(nullable = false)
  private String email;

  @Column(name = "license_number")
  private String licenseNumber;

  @Column(name = "logo_url")
  private String logoUrl;

  @Column(columnDefinition = "TEXT")
  private String description;

  private String website;

  @OneToOne
  @JoinColumn(name = "subscription_id")
  private Subscription subscription;

  @Column(name = "is_active", nullable = false)
  private Boolean isActive = false;

  @Column(name = "current_vehicle_count", nullable = false)
  private Integer currentVehicleCount = 0;

  @Column(length = 50)
  private String phone;

  @Column(columnDefinition = "TEXT")
  private String address;

  @Column(name = "tax_id", length = 100)
  private String taxId;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp private LocalDateTime updatedAt;
}
