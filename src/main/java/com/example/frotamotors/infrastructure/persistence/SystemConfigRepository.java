package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.model.SystemConfig;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, UUID> {
  Optional<SystemConfig> findByKey(String key);

  java.util.List<SystemConfig> findByCategory(String category);
}
