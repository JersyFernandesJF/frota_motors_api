package com.example.kubico.infrastructure.persistence;

import com.example.kubico.domain.model.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
  java.util.Optional<User> findByEmail(String email);

  java.util.Optional<User> findByGoogleId(String googleId);

  java.util.Optional<User> findByAppleId(String appleId);

  Page<User> findAll(Pageable pageable);
}
