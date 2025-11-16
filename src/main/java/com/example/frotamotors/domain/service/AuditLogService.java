package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.model.AuditLog;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.infrastructure.persistence.AuditLogRepository;
import com.example.frotamotors.infrastructure.persistence.UserRepository;
import com.example.frotamotors.infrastructure.util.SecurityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class AuditLogService {

  @Autowired private AuditLogRepository auditLogRepository;

  @Autowired private UserRepository userRepository;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Transactional
  public AuditLog log(
      String action,
      String entityType,
      UUID entityId,
      Map<String, Object> oldValues,
      Map<String, Object> newValues,
      Integer statusCode,
      String errorMessage) {
    AuditLog log = new AuditLog();

    UUID userId = SecurityUtils.getCurrentUserId();
    if (userId != null) {
      User user = userRepository.findById(userId).orElse(null);
      log.setUser(user);
    }

    log.setAction(action);
    log.setEntityType(entityType);
    log.setEntityId(entityId);
    log.setStatusCode(statusCode);
    log.setErrorMessage(errorMessage);

    // Get request information
    try {
      ServletRequestAttributes attributes =
          (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      if (attributes != null) {
        HttpServletRequest request = attributes.getRequest();
        log.setIpAddress(getClientIpAddress(request));
        log.setUserAgent(request.getHeader("User-Agent"));
        log.setRequestMethod(request.getMethod());
        log.setRequestPath(request.getRequestURI());
      }
    } catch (Exception e) {
      // Ignore if request context is not available
    }

    // Serialize old and new values
    try {
      if (oldValues != null && !oldValues.isEmpty()) {
        log.setOldValues(objectMapper.writeValueAsString(oldValues));
      }
      if (newValues != null && !newValues.isEmpty()) {
        log.setNewValues(objectMapper.writeValueAsString(newValues));
      }
    } catch (Exception e) {
      // Ignore serialization errors
    }

    return auditLogRepository.save(log);
  }

  public Page<AuditLog> search(
      UUID userId,
      String action,
      String entityType,
      LocalDateTime startDate,
      LocalDateTime endDate,
      Pageable pageable) {
    return auditLogRepository.search(userId, action, entityType, startDate, endDate, pageable);
  }

  private String getClientIpAddress(HttpServletRequest request) {
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
      return xForwardedFor.split(",")[0].trim();
    }
    String xRealIp = request.getHeader("X-Real-IP");
    if (xRealIp != null && !xRealIp.isEmpty()) {
      return xRealIp;
    }
    return request.getRemoteAddr();
  }
}
