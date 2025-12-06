package com.example.frotamotors.infrastructure.web;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.frotamotors.domain.enums.VehicleStatus;
import com.example.frotamotors.domain.enums.VehicleType;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.domain.model.VehicleHistory;
import com.example.frotamotors.domain.service.VehicleService;
import com.example.frotamotors.infrastructure.dto.ExportRequestDTO;
import com.example.frotamotors.infrastructure.dto.PageResponseDTO;
import com.example.frotamotors.infrastructure.dto.VehicleApproveRequestDTO;
import com.example.frotamotors.infrastructure.dto.VehicleBulkApproveRequestDTO;
import com.example.frotamotors.infrastructure.dto.VehicleBulkRejectRequestDTO;
import com.example.frotamotors.infrastructure.dto.VehicleCreateDTO;
import com.example.frotamotors.infrastructure.dto.VehicleHistoryResponseDTO;
import com.example.frotamotors.infrastructure.dto.VehicleRejectRequestDTO;
import com.example.frotamotors.infrastructure.dto.VehicleResponseDTO;
import com.example.frotamotors.infrastructure.dto.VehicleSummaryDTO;
import com.example.frotamotors.infrastructure.mapper.VehicleMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/v1/vehicles")
@RequiredArgsConstructor
@Slf4j
public class VehicleController {

  @Autowired private VehicleService vehicleService;

  @GetMapping("/search")
  @Transactional(readOnly = true)
  public ResponseEntity<PageResponseDTO<VehicleSummaryDTO>> search(
      @RequestParam(required = false) String filter,
      @RequestParam(required = false) VehicleType type,
      @RequestParam(required = false) VehicleStatus status,
      @RequestParam(required = false) BigDecimal minPrice,
      @RequestParam(required = false) BigDecimal maxPrice,
      @RequestParam(required = false) String brand,
      @RequestParam(required = false) String model,
      @RequestParam(required = false) Integer minYear,
      @RequestParam(required = false) Integer maxYear,
      @RequestParam(required = false) String fuelType,
      @RequestParam(required = false) String transmission,
      @RequestParam(required = false) Integer minMileage,
      @RequestParam(required = false) Integer maxMileage,
      @RequestParam(required = false) String location,
      @RequestParam(required = false) String search,
      @RequestParam(required = false) String sort,
      @RequestParam(required = false) java.time.LocalDateTime startDate,
      @RequestParam(required = false) java.time.LocalDateTime endDate,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {

    Page<Vehicle> page;

    // If RSQL filter is provided, use it (preferred method)
    if (filter != null && !filter.trim().isEmpty()) {
      page = vehicleService.search(filter, pageable);
    } else {
      // Otherwise, use the legacy method with individual parameters (for backward compatibility)
      page =
          vehicleService.search(
              type,
              status,
              minPrice,
              maxPrice,
              brand,
              model,
              minYear,
              maxYear,
              fuelType,
              transmission,
              minMileage,
              maxMileage,
              location,
              search,
              sort,
              startDate,
              endDate,
              pageable);
    }

    List<VehicleSummaryDTO> content =
        page.getContent().stream().map(VehicleMapper::toSummary).collect(Collectors.toList());

    PageResponseDTO<VehicleSummaryDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/brands")
  @Transactional(readOnly = true)
  public ResponseEntity<java.util.List<java.util.Map<String, Object>>> getBrands(
      @RequestParam(required = false) String search) {
    java.util.List<Object[]> rows = vehicleService.getBrandSuggestions(search);
    java.util.List<java.util.Map<String, Object>> result =
        rows.stream()
            .map(
                row -> {
                  String brand = (String) row[0];
                  Number count = (Number) row[1];
                  java.util.Map<String, Object> map = new java.util.HashMap<>();
                  map.put("value", brand);
                  map.put("label", brand);
                  map.put("count", count != null ? count.intValue() : 0);
                  return map;
                })
            .toList();
    return ResponseEntity.ok(result);
  }

  @GetMapping("/models")
  @Transactional(readOnly = true)
  public ResponseEntity<java.util.List<java.util.Map<String, Object>>> getModels(
      @RequestParam(required = false) String brand, @RequestParam(required = false) String search) {
    java.util.List<Object[]> rows = vehicleService.getModelSuggestions(brand, search);
    java.util.List<java.util.Map<String, Object>> result =
        rows.stream()
            .map(
                row -> {
                  String model = (String) row[0];
                  Number count = (Number) row[1];
                  java.util.Map<String, Object> map = new java.util.HashMap<>();
                  map.put("value", model);
                  map.put("label", model);
                  map.put("count", count != null ? count.intValue() : 0);
                  return map;
                })
            .toList();
    return ResponseEntity.ok(result);
  }

  @GetMapping
  @Transactional(readOnly = true)
  public ResponseEntity<PageResponseDTO<VehicleSummaryDTO>> getAll(
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Vehicle> page = vehicleService.getAll(pageable);

    List<VehicleSummaryDTO> content =
        page.getContent().stream().map(VehicleMapper::toSummary).collect(Collectors.toList());

    PageResponseDTO<VehicleSummaryDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/owner/{ownerId}")
  @Transactional(readOnly = true)
  public ResponseEntity<PageResponseDTO<VehicleSummaryDTO>> getByOwner(
      @PathVariable UUID ownerId,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Vehicle> page = vehicleService.getByOwner(ownerId, pageable);

    List<VehicleSummaryDTO> content =
        page.getContent().stream().map(VehicleMapper::toSummary).collect(Collectors.toList());

    PageResponseDTO<VehicleSummaryDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/agency/{agencyId}")
  @Transactional(readOnly = true)
  public ResponseEntity<PageResponseDTO<VehicleSummaryDTO>> getByAgency(
      @PathVariable UUID agencyId,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Vehicle> page = vehicleService.getByAgency(agencyId, pageable);

    List<VehicleSummaryDTO> content =
        page.getContent().stream().map(VehicleMapper::toSummary).collect(Collectors.toList());

    PageResponseDTO<VehicleSummaryDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/type/{type}")
  @Transactional(readOnly = true)
  public ResponseEntity<PageResponseDTO<VehicleSummaryDTO>> getByType(
      @PathVariable VehicleType type,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Vehicle> page = vehicleService.getByType(type, pageable);

    List<VehicleSummaryDTO> content =
        page.getContent().stream().map(VehicleMapper::toSummary).collect(Collectors.toList());

    PageResponseDTO<VehicleSummaryDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/status/{status}")
  @Transactional(readOnly = true)
  public ResponseEntity<PageResponseDTO<VehicleSummaryDTO>> getByStatus(
      @PathVariable VehicleStatus status,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Vehicle> page = vehicleService.getByStatus(status, pageable);

    List<VehicleSummaryDTO> content =
        page.getContent().stream().map(VehicleMapper::toSummary).collect(Collectors.toList());

    PageResponseDTO<VehicleSummaryDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("{id}")
  @Transactional(readOnly = true)
  public ResponseEntity<VehicleResponseDTO> getById(@PathVariable UUID id) {
    Vehicle vehicle = vehicleService.getById(id);
    return ResponseEntity.ok(VehicleMapper.toResponse(vehicle));
  }

  @PostMapping
  public ResponseEntity<VehicleResponseDTO> create(@RequestBody VehicleCreateDTO dto) {
    Vehicle saved = vehicleService.create(dto);
    return ResponseEntity.ok(VehicleMapper.toResponse(saved));
  }

  @PutMapping("{id}")
  public ResponseEntity<VehicleResponseDTO> update(
      @PathVariable UUID id, @RequestBody VehicleCreateDTO dto) {
    Vehicle updated = vehicleService.update(id, dto);
    return ResponseEntity.ok(VehicleMapper.toResponse(updated));
  }

  @DeleteMapping("{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    vehicleService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("{id}/approve")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<VehicleResponseDTO> approveVehicle(
      @PathVariable UUID id,
      @Valid @RequestBody(required = false) VehicleApproveRequestDTO request) {
    String notes = request != null ? request.notes() : null;
    Vehicle vehicle = vehicleService.approveVehicle(id, notes);
    return ResponseEntity.ok(VehicleMapper.toResponse(vehicle));
  }

  @PostMapping("{id}/reject")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<VehicleResponseDTO> rejectVehicle(
      @PathVariable UUID id, @Valid @RequestBody VehicleRejectRequestDTO request) {
    Vehicle vehicle = vehicleService.rejectVehicle(id, request.reason(), request.notifySeller());
    return ResponseEntity.ok(VehicleMapper.toResponse(vehicle));
  }

  @GetMapping("{id}/history")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<PageResponseDTO<VehicleHistoryResponseDTO>> getVehicleHistory(
      @PathVariable UUID id,
      @PageableDefault(
              size = 20,
              sort = "changedAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<VehicleHistory> page = vehicleService.getVehicleHistory(id, pageable);

    List<VehicleHistoryResponseDTO> content =
        page.getContent().stream()
            .map(
                history ->
                    new VehicleHistoryResponseDTO(
                        history.getId(),
                        history.getAction(),
                        history.getOldValue(),
                        history.getNewValue(),
                        history.getChangedBy() != null ? history.getChangedBy().getId() : null,
                        history.getChangedBy() != null ? history.getChangedBy().getName() : null,
                        history.getChangedAt()))
            .collect(Collectors.toList());

    PageResponseDTO<VehicleHistoryResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @PostMapping("/bulk-approve")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<java.util.Map<String, Object>> bulkApprove(
      @Valid @RequestBody VehicleBulkApproveRequestDTO request) {
    List<Vehicle> approved = vehicleService.bulkApprove(request.vehicleIds(), request.notes());
    return ResponseEntity.ok(
        java.util.Map.of(
            "message",
            "Vehicles approved successfully",
            "approvedCount",
            approved.size(),
            "totalCount",
            request.vehicleIds().size()));
  }

  @PostMapping("/bulk-reject")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<java.util.Map<String, Object>> bulkReject(
      @Valid @RequestBody VehicleBulkRejectRequestDTO request) {
    List<Vehicle> rejected = vehicleService.bulkReject(request.vehicleIds(), request.reason());
    return ResponseEntity.ok(
        java.util.Map.of(
            "message",
            "Vehicles rejected successfully",
            "rejectedCount",
            rejected.size(),
            "totalCount",
            request.vehicleIds().size()));
  }

  @PostMapping("/export")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<java.util.Map<String, String>> exportVehicles(
      @Valid @RequestBody ExportRequestDTO request) {
    String result = vehicleService.exportVehicles(request);
    return ResponseEntity.ok(java.util.Map.of("message", result));
  }
}
