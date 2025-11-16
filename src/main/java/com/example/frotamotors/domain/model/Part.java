package com.example.frotamotors.domain.model;

import com.example.frotamotors.domain.enums.PartCategory;
import com.example.frotamotors.domain.enums.PartStatus;
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
@Table(name = "parts")
@NoArgsConstructor
public class Part {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "seller_id", nullable = false)
  private User seller;

  @ManyToOne
  @JoinColumn(name = "agency_id")
  private Agency agency;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PartCategory category;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PartStatus status;

  @Column(nullable = false, length = 200)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(nullable = false, length = 3)
  private String currency;

  @Column(name = "part_number", length = 100)
  private String partNumber;

  @Column(name = "oem_number", length = 100)
  private String oemNumber;

  @Column(name = "brand", length = 100)
  private String brand;

  @Column(name = "compatible_vehicles", columnDefinition = "TEXT")
  private String compatibleVehicles;

  @Column(name = "condition_type", length = 50)
  private String conditionType;

  @Column(name = "quantity_available")
  private Integer quantityAvailable;

  @Column(name = "warranty_months")
  private Integer warrantyMonths;

  @OneToMany(
      mappedBy = "part",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  private List<Media> media = new ArrayList<>();

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp private LocalDateTime updatedAt;
}
