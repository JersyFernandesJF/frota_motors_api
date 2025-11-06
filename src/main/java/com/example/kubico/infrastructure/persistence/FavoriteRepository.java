package com.example.kubico.infrastructure.persistence;

import com.example.kubico.domain.model.Favorite;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
  Page<Favorite> findAll(Pageable pageable);

  Page<Favorite> findByUserId(UUID userId, Pageable pageable);
}
