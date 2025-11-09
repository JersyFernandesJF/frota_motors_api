package com.example.frotamotors.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Entity
@Table(name = "user_blocks", uniqueConstraints = @UniqueConstraint(columnNames = {"blocker_id", "blocked_id"}))
@NoArgsConstructor
public class UserBlock {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "blocker_id", nullable = false)
  private User blocker;

  @ManyToOne
  @JoinColumn(name = "blocked_id", nullable = false)
  private User blocked;

  @Column(name = "reason", columnDefinition = "TEXT")
  private String reason;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;
}

