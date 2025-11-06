package com.example.kubico.infrastructure.persistence;

import com.example.kubico.domain.enums.NotificationType;
import com.example.kubico.domain.model.Alerts;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Alerts, UUID> {
  List<Alerts> findByUserId(UUID userId);

  List<Alerts> findByUserIdAndIsRead(UUID userId, Boolean isRead);

  List<Alerts> findByType(NotificationType type);

  @Query(
      "SELECT n FROM Alerts n WHERE "
          + "n.user.id = :userId AND "
          + "(:isRead IS NULL OR n.isRead = :isRead) AND "
          + "(:type IS NULL OR n.type = :type)")
  List<Alerts> search(
      @Param("userId") UUID userId,
      @Param("isRead") Boolean isRead,
      @Param("type") NotificationType type);

  @Query(
      "SELECT n FROM Alerts n WHERE "
          + "n.user.id = :userId AND "
          + "(:isRead IS NULL OR n.isRead = :isRead) AND "
          + "(:type IS NULL OR n.type = :type)")
  Page<Alerts> searchPageable(
      @Param("userId") UUID userId,
      @Param("isRead") Boolean isRead,
      @Param("type") NotificationType type,
      Pageable pageable);

  Page<Alerts> findByUserId(UUID userId, Pageable pageable);

  Page<Alerts> findByUserIdAndIsRead(UUID userId, Boolean isRead, Pageable pageable);

  Long countByUserIdAndIsRead(UUID userId, Boolean isRead);
}

