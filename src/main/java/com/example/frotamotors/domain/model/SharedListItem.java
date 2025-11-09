package com.example.frotamotors.domain.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
    name = "shared_list_items",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"list_id", "property_id"})})
public class SharedListItem {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "list_id", nullable = false)
  private SharedList list;

  @ManyToOne
  @JoinColumn(name = "property_id", nullable = false)
  private Property property;
}
