package com.example.frotamotors.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "system_configs", uniqueConstraints = @UniqueConstraint(columnNames = "key"))
@NoArgsConstructor
public class SystemConfig {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false)
  private UUID id;

  @Column(nullable = false, unique = true, length = 100)
  private String key;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "value", columnDefinition = "jsonb")
  private String value;

  @Column(name = "category", length = 50)
  private String category; // general, moderation, notifications, security

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @ManyToOne
  @JoinColumn(name = "updated_by_id")
  private User updatedBy;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime updatedAt;
}

