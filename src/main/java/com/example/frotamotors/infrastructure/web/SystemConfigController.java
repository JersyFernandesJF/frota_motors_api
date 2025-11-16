package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.model.SystemConfig;
import com.example.frotamotors.domain.service.SystemConfigService;
import com.example.frotamotors.infrastructure.dto.SystemConfigResponseDTO;
import com.example.frotamotors.infrastructure.dto.SystemConfigUpdateDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/settings")
@RequiredArgsConstructor
@Slf4j
public class SystemConfigController {

  @Autowired private SystemConfigService configService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<List<SystemConfigResponseDTO>> getAll() {
    List<SystemConfig> configs = configService.getAll();

    List<SystemConfigResponseDTO> content =
        configs.stream()
            .map(
                config -> {
                  try {
                    Map<String, Object> value = null;
                    if (config.getValue() != null && !config.getValue().isEmpty()) {
                      value =
                          objectMapper.readValue(
                              config.getValue(), new TypeReference<Map<String, Object>>() {});
                    }
                    return new SystemConfigResponseDTO(
                        config.getId(),
                        config.getKey(),
                        value,
                        config.getCategory(),
                        config.getDescription(),
                        config.getUpdatedBy() != null ? config.getUpdatedBy().getId() : null,
                        config.getUpdatedBy() != null ? config.getUpdatedBy().getName() : null,
                        config.getCreatedAt(),
                        config.getUpdatedAt());
                  } catch (Exception e) {
                    return new SystemConfigResponseDTO(
                        config.getId(),
                        config.getKey(),
                        null,
                        config.getCategory(),
                        config.getDescription(),
                        config.getUpdatedBy() != null ? config.getUpdatedBy().getId() : null,
                        config.getUpdatedBy() != null ? config.getUpdatedBy().getName() : null,
                        config.getCreatedAt(),
                        config.getUpdatedAt());
                  }
                })
            .collect(Collectors.toList());

    return ResponseEntity.ok(content);
  }

  @GetMapping("/category/{category}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<List<SystemConfigResponseDTO>> getByCategory(
      @PathVariable String category) {
    List<SystemConfig> configs = configService.getByCategory(category);

    List<SystemConfigResponseDTO> content =
        configs.stream()
            .map(
                config -> {
                  try {
                    Map<String, Object> value = null;
                    if (config.getValue() != null && !config.getValue().isEmpty()) {
                      value =
                          objectMapper.readValue(
                              config.getValue(), new TypeReference<Map<String, Object>>() {});
                    }
                    return new SystemConfigResponseDTO(
                        config.getId(),
                        config.getKey(),
                        value,
                        config.getCategory(),
                        config.getDescription(),
                        config.getUpdatedBy() != null ? config.getUpdatedBy().getId() : null,
                        config.getUpdatedBy() != null ? config.getUpdatedBy().getName() : null,
                        config.getCreatedAt(),
                        config.getUpdatedAt());
                  } catch (Exception e) {
                    return new SystemConfigResponseDTO(
                        config.getId(),
                        config.getKey(),
                        null,
                        config.getCategory(),
                        config.getDescription(),
                        config.getUpdatedBy() != null ? config.getUpdatedBy().getId() : null,
                        config.getUpdatedBy() != null ? config.getUpdatedBy().getName() : null,
                        config.getCreatedAt(),
                        config.getUpdatedAt());
                  }
                })
            .collect(Collectors.toList());

    return ResponseEntity.ok(content);
  }

  @GetMapping("/{key}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<SystemConfigResponseDTO> getByKey(@PathVariable String key) {
    SystemConfig config = configService.getByKey(key);

    try {
      Map<String, Object> value = null;
      if (config.getValue() != null && !config.getValue().isEmpty()) {
        value =
            objectMapper.readValue(config.getValue(), new TypeReference<Map<String, Object>>() {});
      }
      SystemConfigResponseDTO response =
          new SystemConfigResponseDTO(
              config.getId(),
              config.getKey(),
              value,
              config.getCategory(),
              config.getDescription(),
              config.getUpdatedBy() != null ? config.getUpdatedBy().getId() : null,
              config.getUpdatedBy() != null ? config.getUpdatedBy().getName() : null,
              config.getCreatedAt(),
              config.getUpdatedAt());
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      SystemConfigResponseDTO response =
          new SystemConfigResponseDTO(
              config.getId(),
              config.getKey(),
              null,
              config.getCategory(),
              config.getDescription(),
              config.getUpdatedBy() != null ? config.getUpdatedBy().getId() : null,
              config.getUpdatedBy() != null ? config.getUpdatedBy().getName() : null,
              config.getCreatedAt(),
              config.getUpdatedAt());
      return ResponseEntity.ok(response);
    }
  }

  @PutMapping("/{key}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<SystemConfigResponseDTO> update(
      @PathVariable String key, @Valid @RequestBody SystemConfigUpdateDTO dto) {
    SystemConfig updated = configService.update(key, dto);

    try {
      Map<String, Object> value = null;
      if (updated.getValue() != null && !updated.getValue().isEmpty()) {
        value =
            objectMapper.readValue(updated.getValue(), new TypeReference<Map<String, Object>>() {});
      }
      SystemConfigResponseDTO response =
          new SystemConfigResponseDTO(
              updated.getId(),
              updated.getKey(),
              value,
              updated.getCategory(),
              updated.getDescription(),
              updated.getUpdatedBy() != null ? updated.getUpdatedBy().getId() : null,
              updated.getUpdatedBy() != null ? updated.getUpdatedBy().getName() : null,
              updated.getCreatedAt(),
              updated.getUpdatedAt());
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      SystemConfigResponseDTO response =
          new SystemConfigResponseDTO(
              updated.getId(),
              updated.getKey(),
              null,
              updated.getCategory(),
              updated.getDescription(),
              updated.getUpdatedBy() != null ? updated.getUpdatedBy().getId() : null,
              updated.getUpdatedBy() != null ? updated.getUpdatedBy().getName() : null,
              updated.getCreatedAt(),
              updated.getUpdatedAt());
      return ResponseEntity.ok(response);
    }
  }
}
