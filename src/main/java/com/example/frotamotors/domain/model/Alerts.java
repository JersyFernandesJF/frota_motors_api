package com.example.frotamotors.domain.model;

import com.example.frotamotors.domain.enums.NotificationType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Entity
@Table(name = "notifications")
@NoArgsConstructor
public class Alerts {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NotificationType type;

  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String message;

  @Column(name = "is_read", nullable = false)
  private Boolean isRead = false;

  @Column(name = "related_entity_type", length = 50)
  private String relatedEntityType;

  @Column(name = "related_entity_id")
  private UUID relatedEntityId;

  @Column(name = "action_url", length = 500)
  private String actionUrl;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;
}
