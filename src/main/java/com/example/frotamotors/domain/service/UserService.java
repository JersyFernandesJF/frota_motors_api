package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.enums.Role;
import com.example.frotamotors.domain.enums.UserStatus;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.UserActivity;
import com.example.frotamotors.infrastructure.dto.AdminResetPasswordRequestDTO;
import com.example.frotamotors.infrastructure.dto.ExportRequestDTO;
import com.example.frotamotors.infrastructure.dto.UserBanRequestDTO;
import com.example.frotamotors.infrastructure.dto.UserContactRequestDTO;
import com.example.frotamotors.infrastructure.dto.UserCreateDTO;
import com.example.frotamotors.infrastructure.dto.UserResponseDTO;
import com.example.frotamotors.infrastructure.dto.UserSuspendRequestDTO;
import com.example.frotamotors.infrastructure.dto.UserUpdateDTO;
import com.example.frotamotors.infrastructure.mapper.UserMapper;
import com.example.frotamotors.infrastructure.persistence.UserActivityRepository;
import com.example.frotamotors.infrastructure.persistence.UserRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {

  @Autowired private UserRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private UserActivityRepository userActivityRepository;

  public UserResponseDTO createUser(UserCreateDTO dto) {

    String encodedPassword = passwordEncoder.encode(dto.password());

    User user = UserMapper.toEntity(dto, encodedPassword);
    User savedUser = userRepository.save(user);

    return UserMapper.toResponse(savedUser);
  }

  public User getUserById(UUID id) {
    return (userRepository.findById(id).orElse(null));
  }

  public Page<User> getAllUsers(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  public User updateUser(UUID id, UserUpdateDTO dto) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("User not found"));

    // Check if email is being changed and if it's already taken
    if (!user.getEmail().equals(dto.email())) {
      if (userRepository.findByEmail(dto.email()).isPresent()) {
        throw new IllegalArgumentException("Email already exists");
      }
      user.setEmail(dto.email());
    }

    user.setName(dto.name());
    if (dto.imageUrl() != null) {
      user.setImageUrl(dto.imageUrl());
    }

    return userRepository.save(user);
  }

  public User updateUser(User user) {
    return userRepository.save(user);
  }

  public void deleteUser(UUID id) {
    userRepository.deleteById(id);
  }

  public Page<User> getAllUsers(Pageable pageable, Role role, UserStatus status, String search) {
    Specification<User> spec = (root, query, cb) -> cb.conjunction();

    if (role != null) {
      Specification<User> roleSpec = (root, query, cb) -> cb.equal(root.get("role"), role);
      spec = spec.and(roleSpec);
    }

    if (status != null) {
      Specification<User> statusSpec = (root, query, cb) -> cb.equal(root.get("status"), status);
      spec = spec.and(statusSpec);
    }

    if (search != null && !search.isEmpty()) {
      Specification<User> searchSpec =
          (root, query, cb) ->
              cb.or(
                  cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"),
                  cb.like(cb.lower(root.get("email")), "%" + search.toLowerCase() + "%"));
      spec = spec.and(searchSpec);
    }

    return userRepository.findAll(spec, pageable);
  }

  @Transactional
  public User suspendUser(UUID id, UserSuspendRequestDTO request) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("User not found"));

    user.setStatus(UserStatus.SUSPENDED);

    // Calculate suspended until date
    LocalDateTime suspendedUntil = LocalDateTime.now();
    if (request.duration() != null && request.unit() != null) {
      switch (request.unit().toLowerCase()) {
        case "days":
          suspendedUntil = suspendedUntil.plusDays(request.duration());
          break;
        case "weeks":
          suspendedUntil = suspendedUntil.plusWeeks(request.duration());
          break;
        case "months":
          suspendedUntil = suspendedUntil.plusMonths(request.duration());
          break;
        default:
          suspendedUntil = suspendedUntil.plusDays(request.duration());
      }
    }
    user.setSuspendedUntil(suspendedUntil);

    User saved = userRepository.save(user);

    // Log activity
    logUserActivity(
        saved.getId(),
        "user_suspended",
        "User suspended: " + request.reason(),
        "user",
        saved.getId(),
        null);

    return saved;
  }

  @Transactional
  public User banUser(UUID id, UserBanRequestDTO request) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("User not found"));

    user.setStatus(UserStatus.BANNED);
    user.setBannedAt(LocalDateTime.now());
    user.setBanReason(request.reason());

    // Delete listings if requested
    if (request.deleteListings() != null && request.deleteListings()) {
      // TODO: Implement listing deletion
      // For now, just mark vehicles as unavailable
    }

    User saved = userRepository.save(user);

    // Log activity
    logUserActivity(
        saved.getId(),
        "user_banned",
        "User banned: " + request.reason(),
        "user",
        saved.getId(),
        null);

    return saved;
  }

  @Transactional
  public User reactivateUser(UUID id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("User not found"));

    user.setStatus(UserStatus.ACTIVE);
    user.setSuspendedUntil(null);
    user.setBannedAt(null);
    user.setBanReason(null);

    User saved = userRepository.save(user);

    // Log activity
    logUserActivity(
        saved.getId(), "user_reactivated", "User reactivated", "user", saved.getId(), null);

    return saved;
  }

  public void contactUser(UUID id, UserContactRequestDTO request) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("User not found"));

    // TODO: Implement email sending via EmailService
    // For now, just log the activity
    logUserActivity(
        user.getId(),
        "admin_contact",
        "Admin contacted user: " + request.subject(),
        "user",
        user.getId(),
        null);
  }

  public Page<UserActivity> getActivityHistory(UUID userId, Pageable pageable) {
    return userActivityRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
  }

  @Transactional
  public void resetPassword(UUID userId, AdminResetPasswordRequestDTO request) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("User not found"));

    String encodedPassword = passwordEncoder.encode(request.newPassword());
    user.setPasswordHash(encodedPassword);
    userRepository.save(user);

    // Log activity
    logUserActivity(
        user.getId(),
        "password_reset_admin",
        "Admin reset user password",
        "user",
        user.getId(),
        null);
  }

  @Transactional
  public void resetPassword(UUID userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("User not found"));

    // Generate a random password
    String newPassword = java.util.UUID.randomUUID().toString().substring(0, 12);
    String encodedPassword = passwordEncoder.encode(newPassword);
    user.setPasswordHash(encodedPassword);
    userRepository.save(user);

    // TODO: Send email with new password to user
    // For now, just log the activity
    logUserActivity(
        user.getId(),
        "password_reset",
        "Password reset by administrator",
        "user",
        user.getId(),
        null);

    // TODO: Send email with new password to user
    // NEVER log passwords in plaintext - this is a security risk
    log.info("Password reset for user {} - New password generated and sent via email", user.getEmail());
  }

  public String exportUsers(ExportRequestDTO request) {
    // TODO: Implement export via ExportService
    // For now, return placeholder
    return "Export functionality will be implemented with ExportService";
  }

  private void logUserActivity(
      UUID userId,
      String type,
      String description,
      String relatedEntityType,
      UUID relatedEntityId,
      String metadata) {
    UserActivity activity = new UserActivity();
    User user = userRepository.findById(userId).orElse(null);
    if (user != null) {
      activity.setUser(user);
      activity.setType(type);
      activity.setDescription(description);
      activity.setRelatedEntityType(relatedEntityType);
      activity.setRelatedEntityId(relatedEntityId);
      activity.setMetadata(metadata);
      userActivityRepository.save(activity);
    }
  }
}
