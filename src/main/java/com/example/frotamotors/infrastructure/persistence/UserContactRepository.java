package com.example.frotamotors.infrastructure.persistence;

import com.example.frotamotors.domain.model.UserContact;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserContactRepository extends JpaRepository<UserContact, UUID> {
  List<UserContact> findByUserId(UUID userId);
}
