package com.example.kubico.domain.model;

import com.example.kubico.domain.enums.ContactType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_contacts")
public class UserContact {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(name = "contact_type", nullable = false)
  private ContactType contactType;

  @Column(name = "contact_value", nullable = false, length = 255)
  private String contactValue;

  @Column(name = "is_primary", nullable = false)
  private Boolean isPrimary = false;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp private LocalDateTime updatedAt;
}
