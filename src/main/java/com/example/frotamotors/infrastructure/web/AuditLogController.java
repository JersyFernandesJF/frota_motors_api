package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.model.AuditLog;
import com.example.frotamotors.domain.service.AuditLogService;
import com.example.frotamotors.infrastructure.dto.AuditLogResponseDTO;
import com.example.frotamotors.infrastructure.dto.PageResponseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/v1/audit-logs")
@RequiredArgsConstructor
@Slf4j
public class AuditLogController {

  @Autowired private AuditLogService auditLogService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<PageResponseDTO<AuditLogResponseDTO>> search(
      @RequestParam(required = false) UUID userId,
      @RequestParam(required = false) String action,
      @RequestParam(required = false) String entityType,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          LocalDateTime endDate,
      @PageableDefault(
              size = 50,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<AuditLog> page =
        auditLogService.search(userId, action, entityType, startDate, endDate, pageable);

    List<AuditLogResponseDTO> content =
        page.getContent().stream()
            .map(
                log -> {
                  try {
                    Map<String, Object> oldValues = null;
                    if (log.getOldValues() != null && !log.getOldValues().isEmpty()) {
                      oldValues =
                          objectMapper.readValue(
                              log.getOldValues(), new TypeReference<Map<String, Object>>() {});
                    }
                    Map<String, Object> newValues = null;
                    if (log.getNewValues() != null && !log.getNewValues().isEmpty()) {
                      newValues =
                          objectMapper.readValue(
                              log.getNewValues(), new TypeReference<Map<String, Object>>() {});
                    }
                    return new AuditLogResponseDTO(
                        log.getId(),
                        log.getUser() != null ? log.getUser().getId() : null,
                        log.getUser() != null ? log.getUser().getName() : null,
                        log.getAction(),
                        log.getEntityType(),
                        log.getEntityId(),
                        log.getIpAddress(),
                        log.getUserAgent(),
                        log.getRequestMethod(),
                        log.getRequestPath(),
                        oldValues,
                        newValues,
                        log.getStatusCode(),
                        log.getErrorMessage(),
                        log.getCreatedAt());
                  } catch (Exception e) {
                    return new AuditLogResponseDTO(
                        log.getId(),
                        log.getUser() != null ? log.getUser().getId() : null,
                        log.getUser() != null ? log.getUser().getName() : null,
                        log.getAction(),
                        log.getEntityType(),
                        log.getEntityId(),
                        log.getIpAddress(),
                        log.getUserAgent(),
                        log.getRequestMethod(),
                        log.getRequestPath(),
                        null,
                        null,
                        log.getStatusCode(),
                        log.getErrorMessage(),
                        log.getCreatedAt());
                  }
                })
            .collect(Collectors.toList());

    PageResponseDTO<AuditLogResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }
}
