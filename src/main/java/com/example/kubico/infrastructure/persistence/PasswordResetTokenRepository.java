package com.example.kubico.infrastructure.persistence;

import com.example.kubico.domain.model.PasswordResetToken;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> {
  Optional<PasswordResetToken> findByToken(String token);

  Optional<PasswordResetToken> findByTokenAndUsedFalse(String token);

  @Modifying
  @Query("UPDATE PasswordResetToken p SET p.used = true WHERE p.user.id = :userId")
  void invalidateAllUserTokens(@Param("userId") UUID userId);
}

