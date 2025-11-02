package com.example.kubico.domain.model;

import com.example.kubico.domain.enums.PropertyStatus;
import com.example.kubico.domain.enums.PropertyType;
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
@Table(name = "properties")
@NoArgsConstructor
public class Property {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "owner_id", nullable = false)
  private User owner;

  @ManyToOne
  @JoinColumn(name = "agency_id")
  private Agency agency;

  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PropertyType type;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PropertyStatus status;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(nullable = false, length = 3)
  private String currency;

  @Column(name = "area_m2")
  private Double areaM2;

  private Integer rooms;
  private Integer bathrooms;
  private Integer floor;
  private Integer totalFloors;
  private Integer yearBuilt;
  private String energyCertificate;

  @OneToMany(
      mappedBy = "property",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  private List<Media> media = new ArrayList<>();

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp private LocalDateTime updatedAt;
}
