package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.model.UserActivity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, UUID> {
  Page<UserActivity> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);
}

