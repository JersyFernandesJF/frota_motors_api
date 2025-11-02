package com.example.kubico.domain.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
    name = "shared_list_members",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"list_id", "user_id"})})
public class SharedListMember {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "list_id", nullable = false)
  private SharedList list;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
