package com.example.kubico.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "shared_lists")
public class SharedList {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false, nullable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "owner_id", nullable = false)
  private User owner;

  @Column(nullable = false)
  private String name;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<SharedListItem> items;

  @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<SharedListMember> members;
}
