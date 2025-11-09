package com.example.frotamotors.domain.model;

import com.example.frotamotors.domain.enums.RentalStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Entity
@Table(name = "vehicle_rentals")
@NoArgsConstructor
public class VehicleRental {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "vehicle_id", nullable = false)
  private Vehicle vehicle;

  @ManyToOne
  @JoinColumn(name = "renter_id", nullable = false)
  private User renter;

  @ManyToOne
  @JoinColumn(name = "agency_id")
  private Agency agency;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RentalStatus status;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  @Column(name = "actual_return_date")
  private LocalDate actualReturnDate;

  @Column(name = "daily_rate", nullable = false)
  private BigDecimal dailyRate;

  @Column(nullable = false, length = 3)
  private String currency;

  @Column(name = "total_amount")
  private BigDecimal totalAmount;

  @Column(name = "deposit_amount")
  private BigDecimal depositAmount;

  @Column(name = "deposit_returned")
  private Boolean depositReturned;

  @Column(columnDefinition = "TEXT")
  private String notes;

  @Column(name = "pickup_location", length = 255)
  private String pickupLocation;

  @Column(name = "return_location", length = 255)
  private String returnLocation;

  @Column(name = "mileage_at_pickup")
  private Integer mileageAtPickup;

  @Column(name = "mileage_at_return")
  private Integer mileageAtReturn;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp private LocalDateTime updatedAt;
}

