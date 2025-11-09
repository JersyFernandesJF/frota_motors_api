package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.enums.InspectionStatus;
import com.example.frotamotors.domain.model.Inspection;
import com.example.frotamotors.domain.service.InspectionService;
import com.example.frotamotors.infrastructure.dto.InspectionCancelRequestDTO;
import com.example.frotamotors.infrastructure.dto.InspectionCreateDTO;
import com.example.frotamotors.infrastructure.dto.InspectionReportUploadDTO;
import com.example.frotamotors.infrastructure.dto.InspectionRescheduleRequestDTO;
import com.example.frotamotors.infrastructure.dto.InspectionResponseDTO;
import com.example.frotamotors.infrastructure.dto.InspectionUpdateDTO;
import com.example.frotamotors.infrastructure.dto.PageResponseDTO;
import com.example.frotamotors.infrastructure.mapper.InspectionMapper;
import jakarta.validation.Valid;
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
@RequestMapping("api/v1/inspections")
@RequiredArgsConstructor
@Slf4j
public class InspectionController {

  @Autowired private InspectionService inspectionService;

  @PostMapping
  public ResponseEntity<InspectionResponseDTO> create(@Valid @RequestBody InspectionCreateDTO dto) {
    Inspection saved = inspectionService.create(dto);
    return ResponseEntity.ok(InspectionMapper.toResponse(saved));
  }

  @GetMapping("{id}")
  public ResponseEntity<InspectionResponseDTO> getById(@PathVariable UUID id) {
    Inspection inspection = inspectionService.getById(id);
    return ResponseEntity.ok(InspectionMapper.toResponse(inspection));
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<PageResponseDTO<InspectionResponseDTO>> getAll(
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<Inspection> page = inspectionService.getAll(pageable);

    List<InspectionResponseDTO> content =
        page.getContent().stream()
            .map(InspectionMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<InspectionResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/search")
  public ResponseEntity<PageResponseDTO<InspectionResponseDTO>> search(
      @RequestParam(required = false) UUID buyerId,
      @RequestParam(required = false) UUID sellerId,
      @RequestParam(required = false) UUID inspectorId,
      @RequestParam(required = false) UUID vehicleId,
      @RequestParam(required = false) InspectionStatus status,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<Inspection> page =
        inspectionService.search(buyerId, sellerId, inspectorId, vehicleId, status, pageable);

    List<InspectionResponseDTO> content =
        page.getContent().stream()
            .map(InspectionMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<InspectionResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @PutMapping("{id}")
  public ResponseEntity<InspectionResponseDTO> update(
      @PathVariable UUID id, @Valid @RequestBody InspectionUpdateDTO dto) {
    Inspection updated = inspectionService.update(id, dto);
    return ResponseEntity.ok(InspectionMapper.toResponse(updated));
  }

  @PostMapping("{id}/confirm")
  public ResponseEntity<InspectionResponseDTO> confirm(@PathVariable UUID id) {
    Inspection confirmed = inspectionService.confirm(id);
    return ResponseEntity.ok(InspectionMapper.toResponse(confirmed));
  }

  @PostMapping("{id}/reschedule")
  public ResponseEntity<InspectionResponseDTO> reschedule(
      @PathVariable UUID id, @Valid @RequestBody InspectionRescheduleRequestDTO request) {
    Inspection rescheduled = inspectionService.reschedule(id, request);
    return ResponseEntity.ok(InspectionMapper.toResponse(rescheduled));
  }

  @PostMapping("{id}/cancel")
  public ResponseEntity<InspectionResponseDTO> cancel(
      @PathVariable UUID id, @Valid @RequestBody InspectionCancelRequestDTO request) {
    Inspection cancelled = inspectionService.cancel(id, request);
    return ResponseEntity.ok(InspectionMapper.toResponse(cancelled));
  }

  @PostMapping("{id}/assign-inspector")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<InspectionResponseDTO> assignInspector(
      @PathVariable UUID id, @RequestParam UUID inspectorId) {
    Inspection updated = inspectionService.assignInspector(id, inspectorId);
    return ResponseEntity.ok(InspectionMapper.toResponse(updated));
  }

  @PostMapping("{id}/upload-report")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<InspectionResponseDTO> uploadReport(
      @PathVariable UUID id, @Valid @RequestBody InspectionReportUploadDTO request) {
    Inspection updated = inspectionService.uploadReport(id, request);
    return ResponseEntity.ok(InspectionMapper.toResponse(updated));
  }

  @DeleteMapping("{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    inspectionService.delete(id);
    return ResponseEntity.noContent().build();
  }
}

