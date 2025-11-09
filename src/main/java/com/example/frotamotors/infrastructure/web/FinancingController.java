package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.enums.FinancingStatus;
import com.example.frotamotors.domain.model.Financing;
import com.example.frotamotors.domain.service.FinancingService;
import com.example.frotamotors.infrastructure.dto.CreditScoreSimulationDTO;
import com.example.frotamotors.infrastructure.dto.FinancingCreateDTO;
import com.example.frotamotors.infrastructure.dto.FinancingRejectRequestDTO;
import com.example.frotamotors.infrastructure.dto.FinancingResponseDTO;
import com.example.frotamotors.infrastructure.dto.PageResponseDTO;
import com.example.frotamotors.infrastructure.mapper.FinancingMapper;
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
@RequestMapping("api/v1/financings")
@RequiredArgsConstructor
@Slf4j
public class FinancingController {

  @Autowired private FinancingService financingService;

  @PostMapping
  public ResponseEntity<FinancingResponseDTO> create(@Valid @RequestBody FinancingCreateDTO dto) {
    Financing saved = financingService.create(dto);
    return ResponseEntity.ok(FinancingMapper.toResponse(saved));
  }

  @GetMapping("{id}")
  public ResponseEntity<FinancingResponseDTO> getById(@PathVariable UUID id) {
    Financing financing = financingService.getById(id);
    return ResponseEntity.ok(FinancingMapper.toResponse(financing));
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<PageResponseDTO<FinancingResponseDTO>> getAll(
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<Financing> page = financingService.getAll(pageable);

    List<FinancingResponseDTO> content =
        page.getContent().stream()
            .map(FinancingMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<FinancingResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/search")
  public ResponseEntity<PageResponseDTO<FinancingResponseDTO>> search(
      @RequestParam(required = false) UUID buyerId,
      @RequestParam(required = false) UUID sellerId,
      @RequestParam(required = false) UUID vehicleId,
      @RequestParam(required = false) FinancingStatus status,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<Financing> page =
        financingService.search(buyerId, sellerId, vehicleId, status, pageable);

    List<FinancingResponseDTO> content =
        page.getContent().stream()
            .map(FinancingMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<FinancingResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @PostMapping("{id}/approve")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<FinancingResponseDTO> approve(@PathVariable UUID id) {
    Financing approved = financingService.approve(id);
    return ResponseEntity.ok(FinancingMapper.toResponse(approved));
  }

  @PostMapping("{id}/reject")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<FinancingResponseDTO> reject(
      @PathVariable UUID id, @Valid @RequestBody FinancingRejectRequestDTO request) {
    Financing rejected = financingService.reject(id, request);
    return ResponseEntity.ok(FinancingMapper.toResponse(rejected));
  }

  @PostMapping("/simulate-credit-score")
  public ResponseEntity<java.util.Map<String, Integer>> simulateCreditScore(
      @Valid @RequestBody CreditScoreSimulationDTO request) {
    Integer score = financingService.simulateCreditScore(request);
    return ResponseEntity.ok(java.util.Map.of("creditScore", score));
  }

  @PutMapping("{id}/credit-score")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<FinancingResponseDTO> updateCreditScore(
      @PathVariable UUID id, @RequestParam Integer creditScore) {
    Financing updated = financingService.updateCreditScore(id, creditScore);
    return ResponseEntity.ok(FinancingMapper.toResponse(updated));
  }

  @DeleteMapping("{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    financingService.delete(id);
    return ResponseEntity.noContent().build();
  }
}

