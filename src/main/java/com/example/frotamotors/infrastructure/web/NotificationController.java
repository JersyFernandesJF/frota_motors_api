package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.enums.NotificationType;
import com.example.frotamotors.domain.model.Alerts;
import com.example.frotamotors.domain.service.NotificationService;
import com.example.frotamotors.infrastructure.dto.NotificationCreateDTO;
import com.example.frotamotors.infrastructure.dto.NotificationResponseDTO;
import com.example.frotamotors.infrastructure.dto.PageResponseDTO;
import com.example.frotamotors.infrastructure.mapper.NotificationMapper;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/v1/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

  @Autowired private NotificationService notificationService;

  @GetMapping("/search")
  public ResponseEntity<PageResponseDTO<NotificationResponseDTO>> search(
      @RequestParam UUID userId,
      @RequestParam(required = false) Boolean isRead,
      @RequestParam(required = false) NotificationType type,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {

    Page<Alerts> page = notificationService.search(userId, isRead, type, pageable);

    List<NotificationResponseDTO> content =
        page.getContent().stream().map(NotificationMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<NotificationResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<PageResponseDTO<NotificationResponseDTO>> getAll(
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Alerts> page = notificationService.getAll(pageable);

    List<NotificationResponseDTO> content =
        page.getContent().stream().map(NotificationMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<NotificationResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<PageResponseDTO<NotificationResponseDTO>> getByUser(
      @PathVariable UUID userId,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Alerts> page = notificationService.getByUser(userId, pageable);

    List<NotificationResponseDTO> content =
        page.getContent().stream().map(NotificationMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<NotificationResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/user/{userId}/unread")
  public ResponseEntity<PageResponseDTO<NotificationResponseDTO>> getUnreadByUser(
      @PathVariable UUID userId,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Alerts> page = notificationService.getUnreadByUser(userId, pageable);

    List<NotificationResponseDTO> content =
        page.getContent().stream().map(NotificationMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<NotificationResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/user/{userId}/unread-count")
  public ResponseEntity<Long> getUnreadCount(@PathVariable UUID userId) {
    Long count = notificationService.getUnreadCount(userId);
    return ResponseEntity.ok(count);
  }

  @GetMapping("{id}")
  public ResponseEntity<NotificationResponseDTO> getById(@PathVariable UUID id) {
    Alerts notification = notificationService.getById(id);
    return ResponseEntity.ok(NotificationMapper.toResponse(notification));
  }

  @PostMapping
  public ResponseEntity<NotificationResponseDTO> create(@RequestBody NotificationCreateDTO dto) {
    Alerts saved = notificationService.create(dto);
    return ResponseEntity.ok(NotificationMapper.toResponse(saved));
  }

  @PostMapping("{id}/read")
  public ResponseEntity<NotificationResponseDTO> markAsRead(@PathVariable UUID id) {
    Alerts notification = notificationService.markAsRead(id);
    return ResponseEntity.ok(NotificationMapper.toResponse(notification));
  }

  @PostMapping("/user/{userId}/read-all")
  public ResponseEntity<Void> markAllAsRead(@PathVariable UUID userId) {
    notificationService.markAllAsRead(userId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    notificationService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
