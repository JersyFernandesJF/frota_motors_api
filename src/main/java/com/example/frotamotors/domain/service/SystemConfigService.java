package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.model.SystemConfig;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.infrastructure.dto.SystemConfigUpdateDTO;
import com.example.frotamotors.infrastructure.persistence.SystemConfigRepository;
import com.example.frotamotors.infrastructure.persistence.UserRepository;
import com.example.frotamotors.infrastructure.util.SecurityUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemConfigService {

  @Autowired private SystemConfigRepository configRepository;

  @Autowired private UserRepository userRepository;

  private final ObjectMapper objectMapper = new ObjectMapper();

  public SystemConfig getByKey(String key) {
    return configRepository
        .findByKey(key)
        .orElseThrow(() -> new EntityNotFoundException("Config not found: " + key));
  }

  public List<SystemConfig> getByCategory(String category) {
    return configRepository.findByCategory(category);
  }

  public List<SystemConfig> getAll() {
    return configRepository.findAll();
  }

  @Transactional
  public SystemConfig update(String key, SystemConfigUpdateDTO dto) {
    SystemConfig config = getByKey(key);
    UUID adminId = SecurityUtils.getCurrentUserId();
    User admin = userRepository.findById(adminId).orElse(null);

    try {
      String valueJson = objectMapper.writeValueAsString(dto.value());
      config.setValue(valueJson);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid value format", e);
    }

    if (dto.description() != null) {
      config.setDescription(dto.description());
    }

    config.setUpdatedBy(admin);
    return configRepository.save(config);
  }

  public Map<String, Object> getValueAsMap(String key) {
    SystemConfig config = getByKey(key);
    try {
      if (config.getValue() == null || config.getValue().isEmpty()) {
        return new HashMap<>();
      }
      return objectMapper.readValue(config.getValue(), new TypeReference<Map<String, Object>>() {});
    } catch (Exception e) {
      return new HashMap<>();
    }
  }

  @Transactional
  public SystemConfig create(
      String key, Map<String, Object> value, String category, String description) {
    Optional<SystemConfig> existing = configRepository.findByKey(key);
    if (existing.isPresent()) {
      throw new IllegalArgumentException("Config with key already exists: " + key);
    }

    SystemConfig config = new SystemConfig();
    config.setKey(key);
    config.setCategory(category);
    config.setDescription(description);

    try {
      String valueJson = objectMapper.writeValueAsString(value);
      config.setValue(valueJson);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid value format", e);
    }

    UUID adminId = SecurityUtils.getCurrentUserId();
    User admin = userRepository.findById(adminId).orElse(null);
    config.setUpdatedBy(admin);

    return configRepository.save(config);
  }
}
