package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.model.AuditLog;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
  Page<AuditLog> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

  Page<AuditLog> findByActionOrderByCreatedAtDesc(String action, Pageable pageable);

  Page<AuditLog> findByEntityTypeOrderByCreatedAtDesc(String entityType, Pageable pageable);

  @Query(
      "SELECT a FROM AuditLog a WHERE "
          + "(:userId IS NULL OR a.user.id = :userId) AND "
          + "(:action IS NULL OR a.action = :action) AND "
          + "(:entityType IS NULL OR a.entityType = :entityType) AND "
          + "(:startDate IS NULL OR a.createdAt >= :startDate) AND "
          + "(:endDate IS NULL OR a.createdAt <= :endDate)")
  Page<AuditLog> search(
      @Param("userId") UUID userId,
      @Param("action") String action,
      @Param("entityType") String entityType,
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate,
      Pageable pageable);
}

