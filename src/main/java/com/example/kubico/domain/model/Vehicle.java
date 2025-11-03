package com.example.kubico.domain.model;

import com.example.kubico.domain.enums.VehicleStatus;
import com.example.kubico.domain.enums.VehicleType;
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
@Table(name = "vehicles")
@NoArgsConstructor
public class Vehicle {
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

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private VehicleType type;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private VehicleStatus status;

  @Column(nullable = false, length = 100)
  private String brand;

  @Column(nullable = false, length = 100)
  private String model;

  @Column(nullable = false)
  private Integer year;

  @Column(length = 50)
  private String color;

  @Column(name = "license_plate", length = 20)
  private String licensePlate;

  @Column(name = "vin", length = 17)
  private String vin;

  @Column(name = "mileage_km")
  private Integer mileageKm;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(nullable = false, length = 3)
  private String currency;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "fuel_type", length = 50)
  private String fuelType;

  @Column(name = "transmission_type", length = 50)
  private String transmissionType;

  @Column(name = "engine_size")
  private Double engineSize;

  @Column(name = "horse_power")
  private Integer horsePower;

  @Column(name = "number_of_doors")
  private Integer numberOfDoors;

  @Column(name = "number_of_seats")
  private Integer numberOfSeats;

  @Column(name = "previous_owners")
  private Integer previousOwners;

  @Column(name = "accident_history")
  private Boolean accidentHistory;

  @OneToMany(
      mappedBy = "vehicle",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  private List<Media> media = new ArrayList<>();

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp private LocalDateTime updatedAt;
}

