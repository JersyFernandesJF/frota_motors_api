package com.example.frotamotors.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Entity
@Table(name = "audit_logs")
@NoArgsConstructor
public class AuditLog {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false, length = 50)
  private String action; // CREATE, UPDATE, DELETE, APPROVE, REJECT, etc.

  @Column(nullable = false, length = 100)
  private String entityType; // User, Vehicle, Complaint, etc.

  @Column(name = "entity_id")
  private UUID entityId;

  @Column(name = "ip_address", length = 45)
  private String ipAddress;

  @Column(name = "user_agent", length = 500)
  private String userAgent;

  @Column(name = "request_method", length = 10)
  private String requestMethod;

  @Column(name = "request_path", length = 500)
  private String requestPath;

  @Column(name = "old_values", columnDefinition = "jsonb")
  private String oldValues;

  @Column(name = "new_values", columnDefinition = "jsonb")
  private String newValues;

  @Column(name = "status_code")
  private Integer statusCode;

  @Column(name = "error_message", columnDefinition = "TEXT")
  private String errorMessage;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;
}

