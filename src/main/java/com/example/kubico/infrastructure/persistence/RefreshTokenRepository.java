package com.example.kubico.infrastructure.persistence;

import com.example.kubico.domain.model.RefreshToken;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
  Optional<RefreshToken> findByToken(String token);

  Optional<RefreshToken> findByTokenAndRevokedFalse(String token);

  @Modifying
  @Query("UPDATE RefreshToken r SET r.revoked = true WHERE r.user.id = :userId")
  void revokeAllUserTokens(@Param("userId") UUID userId);

  @Modifying
  @Query("DELETE FROM RefreshToken r WHERE r.expiresAt < :now")
  void deleteExpiredTokens(@Param("now") java.time.LocalDateTime now);
}

