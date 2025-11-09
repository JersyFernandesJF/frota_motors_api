package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.enums.VehicleStatus;
import com.example.frotamotors.domain.enums.VehicleType;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.domain.model.VehicleHistory;
import com.example.frotamotors.domain.service.VehicleService;
import com.example.frotamotors.infrastructure.dto.ExportRequestDTO;
import com.example.frotamotors.infrastructure.dto.PageResponseDTO;
import com.example.frotamotors.infrastructure.dto.VehicleApproveRequestDTO;
import com.example.frotamotors.infrastructure.dto.VehicleCreateDTO;
import com.example.frotamotors.infrastructure.dto.VehicleHistoryResponseDTO;
import com.example.frotamotors.infrastructure.dto.VehicleRejectRequestDTO;
import com.example.frotamotors.infrastructure.dto.VehicleResponseDTO;
import com.example.frotamotors.infrastructure.mapper.VehicleMapper;
import jakarta.validation.Valid;
import java.math.BigDecimal;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/vehicles")
@RequiredArgsConstructor
@Slf4j
public class VehicleController {

  @Autowired private VehicleService vehicleService;

  @GetMapping("/search")
  public ResponseEntity<PageResponseDTO<VehicleResponseDTO>> search(
      @RequestParam(required = false) VehicleType type,
      @RequestParam(required = false) VehicleStatus status,
      @RequestParam(required = false) BigDecimal minPrice,
      @RequestParam(required = false) BigDecimal maxPrice,
      @RequestParam(required = false) String brand,
      @RequestParam(required = false) String model,
      @RequestParam(required = false) Integer minYear,
      @RequestParam(required = false) Integer maxYear,
      @RequestParam(required = false) String fuelType,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {

    Page<Vehicle> page =
        vehicleService.search(
            type, status, minPrice, maxPrice, brand, model, minYear, maxYear, fuelType, pageable);

    List<VehicleResponseDTO> content =
        page.getContent().stream()
            .map(VehicleMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<VehicleResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<PageResponseDTO<VehicleResponseDTO>> getAll(
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<Vehicle> page = vehicleService.getAll(pageable);

    List<VehicleResponseDTO> content =
        page.getContent().stream()
            .map(VehicleMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<VehicleResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/owner/{ownerId}")
  public ResponseEntity<PageResponseDTO<VehicleResponseDTO>> getByOwner(
      @PathVariable UUID ownerId,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<Vehicle> page = vehicleService.getByOwner(ownerId, pageable);

    List<VehicleResponseDTO> content =
        page.getContent().stream()
            .map(VehicleMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<VehicleResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/agency/{agencyId}")
  public ResponseEntity<PageResponseDTO<VehicleResponseDTO>> getByAgency(
      @PathVariable UUID agencyId,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<Vehicle> page = vehicleService.getByAgency(agencyId, pageable);

    List<VehicleResponseDTO> content =
        page.getContent().stream()
            .map(VehicleMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<VehicleResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/type/{type}")
  public ResponseEntity<PageResponseDTO<VehicleResponseDTO>> getByType(
      @PathVariable VehicleType type,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<Vehicle> page = vehicleService.getByType(type, pageable);

    List<VehicleResponseDTO> content =
        page.getContent().stream()
            .map(VehicleMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<VehicleResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/status/{status}")
  public ResponseEntity<PageResponseDTO<VehicleResponseDTO>> getByStatus(
      @PathVariable VehicleStatus status,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<Vehicle> page = vehicleService.getByStatus(status, pageable);

    List<VehicleResponseDTO> content =
        page.getContent().stream()
            .map(VehicleMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<VehicleResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("{id}")
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
      @PathVariable UUID id, @Valid @RequestBody(required = false) VehicleApproveRequestDTO request) {
    String notes = request != null ? request.notes() : null;
    Vehicle vehicle = vehicleService.approveVehicle(id, notes);
    return ResponseEntity.ok(VehicleMapper.toResponse(vehicle));
  }

  @PostMapping("{id}/reject")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<VehicleResponseDTO> rejectVehicle(
      @PathVariable UUID id, @Valid @RequestBody VehicleRejectRequestDTO request) {
    Vehicle vehicle =
        vehicleService.rejectVehicle(id, request.reason(), request.notifySeller());
    return ResponseEntity.ok(VehicleMapper.toResponse(vehicle));
  }

  @GetMapping("{id}/history")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<PageResponseDTO<VehicleHistoryResponseDTO>> getVehicleHistory(
      @PathVariable UUID id,
      @PageableDefault(size = 20, sort = "changedAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
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
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @PostMapping("/export")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<java.util.Map<String, String>> exportVehicles(
      @Valid @RequestBody ExportRequestDTO request) {
    String result = vehicleService.exportVehicles(request);
    return ResponseEntity.ok(java.util.Map.of("message", result));
  }
}

