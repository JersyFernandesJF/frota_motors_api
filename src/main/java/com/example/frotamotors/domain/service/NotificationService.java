package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.enums.NotificationType;
import com.example.frotamotors.domain.model.Alerts;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.infrastructure.dto.NotificationCreateDTO;
import com.example.frotamotors.infrastructure.mapper.NotificationMapper;
import com.example.frotamotors.infrastructure.persistence.NotificationRepository;
import com.example.frotamotors.infrastructure.persistence.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

  @Autowired private NotificationRepository notificationRepository;

  @Autowired private UserRepository userRepository;

  public Alerts create(NotificationCreateDTO dto) {
    User user =
        userRepository
            .findById(dto.userId())
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

    Alerts notification = NotificationMapper.toEntity(dto, user);
    return notificationRepository.save(notification);
  }

  public List<Alerts> getAll() {
    return notificationRepository.findAll();
  }

  public Page<Alerts> getAll(Pageable pageable) {
    return notificationRepository.findAll(pageable);
  }

  public Alerts getById(UUID id) {
    return notificationRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
  }

  public Alerts markAsRead(UUID id) {
    Alerts notification = getById(id);
    notification.setIsRead(true);
    return notificationRepository.save(notification);
  }

  public void markAllAsRead(UUID userId) {
    List<Alerts> notifications = notificationRepository.findByUserIdAndIsRead(userId, false);
    notifications.forEach(n -> n.setIsRead(true));
    notificationRepository.saveAll(notifications);
  }

  public void delete(UUID id) {
    if (!notificationRepository.existsById(id)) {
      throw new EntityNotFoundException("Notification not found");
    }
    notificationRepository.deleteById(id);
  }

  public List<Alerts> getByUser(UUID userId) {
    return notificationRepository.findByUserId(userId);
  }

  public Page<Alerts> getByUser(UUID userId, Pageable pageable) {
    return notificationRepository.findByUserId(userId, pageable);
  }

  public List<Alerts> getUnreadByUser(UUID userId) {
    return notificationRepository.findByUserIdAndIsRead(userId, false);
  }

  public Page<Alerts> getUnreadByUser(UUID userId, Pageable pageable) {
    return notificationRepository.findByUserIdAndIsRead(userId, false, pageable);
  }

  public List<Alerts> search(UUID userId, Boolean isRead, NotificationType type) {
    return notificationRepository.search(userId, isRead, type);
  }

  public Page<Alerts> search(
      UUID userId, Boolean isRead, NotificationType type, Pageable pageable) {
    return notificationRepository.searchPageable(userId, isRead, type, pageable);
  }

  public Long getUnreadCount(UUID userId) {
    return notificationRepository.countByUserIdAndIsRead(userId, false);
  }
}
