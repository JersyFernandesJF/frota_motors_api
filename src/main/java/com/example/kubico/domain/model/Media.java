package com.example.kubico.domain.model;

import com.example.kubico.domain.enums.MediaType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "media")
public class Media {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(updatable = false, nullable = false)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "property_id", nullable = false)
  private Property property;

  @Enumerated(EnumType.STRING)
  @Column(name = "media_type", nullable = false)
  private MediaType mediaType;

  @Column(nullable = false)
  private String url;

  @CreationTimestamp
  @Column(name = "uploaded_at", updatable = false)
  private LocalDateTime uploadedAt;
}
