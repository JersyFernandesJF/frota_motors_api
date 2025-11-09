package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.model.UserBlock;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBlockRepository extends JpaRepository<UserBlock, UUID> {
  Optional<UserBlock> findByBlockerIdAndBlockedId(UUID blockerId, UUID blockedId);
  
  boolean existsByBlockerIdAndBlockedId(UUID blockerId, UUID blockedId);
}

