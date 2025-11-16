package com.example.frotamotors.domain.model;

import com.example.frotamotors.domain.enums.Role;
import com.example.frotamotors.domain.enums.UserStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false)
  private UUID id;

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, unique = true, length = 150)
  private String email;

  @Column(nullable = true)
  private String passwordHash;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Role role;

  @Column(name = "google_id", unique = true)
  private String googleId;

  @Column(name = "apple_id", unique = true)
  private String appleId;

  @Column(name = "provider", length = 20)
  private String provider;

  @Column(name = "image_url", length = 500)
  private String imageUrl;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "permissions", columnDefinition = "jsonb")
  private List<String> permissions = new ArrayList<>();

  @Column(name = "last_login")
  private LocalDateTime lastLogin;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 20)
  private UserStatus status = UserStatus.ACTIVE;

  @Column(name = "suspended_until")
  private LocalDateTime suspendedUntil;

  @Column(name = "banned_at")
  private LocalDateTime bannedAt;

  @Column(name = "ban_reason", columnDefinition = "TEXT")
  private String banReason;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserContact> contacts = new ArrayList<>();

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  private LocalDateTime updatedAt;
}
