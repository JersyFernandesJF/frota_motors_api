package com.example.frotamotors.domain.model;

import com.example.frotamotors.domain.enums.OrderStatus;
import com.example.frotamotors.domain.enums.OrderType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Entity
@Table(name = "purchases")
@NoArgsConstructor
public class Purchase {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "buyer_id", nullable = false)
  private User buyer;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderType type;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderStatus status;

  // References to the purchased item (only one should be set)
  @ManyToOne
  @JoinColumn(name = "vehicle_id")
  private Vehicle vehicle;

  @ManyToOne
  @JoinColumn(name = "part_id")
  private Part part;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(nullable = false, length = 3)
  private String currency;

  @Column(name = "quantity")
  private Integer quantity;

  @Column(name = "total_amount", nullable = false)
  private BigDecimal totalAmount;

  @Column(name = "shipping_address", columnDefinition = "TEXT")
  private String shippingAddress;

  @Column(name = "delivery_date")
  private LocalDateTime deliveryDate;

  @Column(name = "tracking_number", length = 100)
  private String trackingNumber;

  @Column(columnDefinition = "TEXT")
  private String notes;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime updatedAt;
}

