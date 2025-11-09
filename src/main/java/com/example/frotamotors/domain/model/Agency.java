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

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp private LocalDateTime updatedAt;
}
