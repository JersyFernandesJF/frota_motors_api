package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.model.User;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
  java.util.Optional<User> findByEmail(String email);

  java.util.Optional<User> findByGoogleId(String googleId);

  java.util.Optional<User> findByAppleId(String appleId);

  Page<User> findAll(Pageable pageable);

  @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= :date")
  Long countByCreatedAtAfter(@Param("date") LocalDateTime date);

  @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt BETWEEN :start AND :end")
  Long countByCreatedAtBetween(
      @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

  @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt < :date")
  Long countByCreatedAtBefore(@Param("date") LocalDateTime date);
}
