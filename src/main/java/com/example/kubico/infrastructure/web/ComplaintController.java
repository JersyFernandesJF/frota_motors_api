package com.example.kubico.infrastructure.web;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.example.kubico.domain.enums.ComplaintStatus;
import com.example.kubico.domain.enums.ComplaintType;
import com.example.kubico.domain.model.Complaint;
import com.example.kubico.domain.service.ComplaintService;
import com.example.kubico.infrastructure.dto.ComplaintCreateDTO;
import com.example.kubico.infrastructure.dto.ComplaintResponseDTO;
import com.example.kubico.infrastructure.dto.PageResponseDTO;
import com.example.kubico.infrastructure.mapper.ComplaintMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/complaints")
@RequiredArgsConstructor
@Slf4j
public class ComplaintController {

  @Autowired private ComplaintService complaintService;

  @GetMapping("/search")
  public ResponseEntity<PageResponseDTO<ComplaintResponseDTO>> search(
      @RequestParam(required = false) ComplaintStatus status,
      @RequestParam(required = false) ComplaintType type,
      @RequestParam(required = false) UUID reporterId,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {

    Page<Complaint> page = complaintService.search(status, type, reporterId, pageable);

    List<ComplaintResponseDTO> content =
        page.getContent().stream()
            .map(ComplaintMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<ComplaintResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<PageResponseDTO<ComplaintResponseDTO>> getAll(
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<Complaint> page = complaintService.getAll(pageable);

    List<ComplaintResponseDTO> content =
        page.getContent().stream()
            .map(ComplaintMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<ComplaintResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/reporter/{reporterId}")
  public ResponseEntity<PageResponseDTO<ComplaintResponseDTO>> getByReporter(
      @PathVariable UUID reporterId,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<Complaint> page = complaintService.getByReporter(reporterId, pageable);

    List<ComplaintResponseDTO> content =
        page.getContent().stream()
            .map(ComplaintMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<ComplaintResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/status/{status}")
  public ResponseEntity<PageResponseDTO<ComplaintResponseDTO>> getByStatus(
      @PathVariable ComplaintStatus status,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<Complaint> page = complaintService.getByStatus(status, pageable);

    List<ComplaintResponseDTO> content =
        page.getContent().stream()
            .map(ComplaintMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<ComplaintResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/type/{type}")
  public ResponseEntity<PageResponseDTO<ComplaintResponseDTO>> getByType(
      @PathVariable ComplaintType type,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<Complaint> page = complaintService.getByType(type, pageable);

    List<ComplaintResponseDTO> content =
        page.getContent().stream()
            .map(ComplaintMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<ComplaintResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/reported-user/{reportedUserId}")
  public ResponseEntity<PageResponseDTO<ComplaintResponseDTO>> getByReportedUser(
      @PathVariable UUID reportedUserId,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<Complaint> page = complaintService.getByReportedUser(reportedUserId, pageable);

    List<ComplaintResponseDTO> content =
        page.getContent().stream()
            .map(ComplaintMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<ComplaintResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/reported-vehicle/{reportedVehicleId}")
  public ResponseEntity<PageResponseDTO<ComplaintResponseDTO>> getByReportedVehicle(
      @PathVariable UUID reportedVehicleId,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<Complaint> page = complaintService.getByReportedVehicle(reportedVehicleId, pageable);

    List<ComplaintResponseDTO> content =
        page.getContent().stream()
            .map(ComplaintMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<ComplaintResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/reported-part/{reportedPartId}")
  public ResponseEntity<PageResponseDTO<ComplaintResponseDTO>> getByReportedPart(
      @PathVariable UUID reportedPartId,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<Complaint> page = complaintService.getByReportedPart(reportedPartId, pageable);

    List<ComplaintResponseDTO> content =
        page.getContent().stream()
            .map(ComplaintMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<ComplaintResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/reported-property/{reportedPropertyId}")
  public ResponseEntity<PageResponseDTO<ComplaintResponseDTO>> getByReportedProperty(
      @PathVariable UUID reportedPropertyId,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<Complaint> page = complaintService.getByReportedProperty(reportedPropertyId, pageable);

    List<ComplaintResponseDTO> content =
        page.getContent().stream()
            .map(ComplaintMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<ComplaintResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/reported-agency/{reportedAgencyId}")
  public ResponseEntity<PageResponseDTO<ComplaintResponseDTO>> getByReportedAgency(
      @PathVariable UUID reportedAgencyId,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<Complaint> page = complaintService.getByReportedAgency(reportedAgencyId, pageable);

    List<ComplaintResponseDTO> content =
        page.getContent().stream()
            .map(ComplaintMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<ComplaintResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("{id}")
  public ResponseEntity<ComplaintResponseDTO> getById(@PathVariable UUID id) {
    Complaint complaint = complaintService.getById(id);
    return ResponseEntity.ok(ComplaintMapper.toResponse(complaint));
  }

  @PostMapping
  public ResponseEntity<ComplaintResponseDTO> create(@RequestBody ComplaintCreateDTO dto) {
    Complaint saved = complaintService.create(dto);
    return ResponseEntity.ok(ComplaintMapper.toResponse(saved));
  }

  @PutMapping("{id}/status")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ComplaintResponseDTO> updateStatus(
      @PathVariable UUID id,
      @RequestParam ComplaintStatus status,
      @RequestParam(required = false) UUID reviewedById,
      @RequestParam(required = false) String notes) {
    Complaint updated = complaintService.updateStatus(id, status, reviewedById, notes);
    return ResponseEntity.ok(ComplaintMapper.toResponse(updated));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    complaintService.delete(id);
    return ResponseEntity.noContent().build();
  }
}

