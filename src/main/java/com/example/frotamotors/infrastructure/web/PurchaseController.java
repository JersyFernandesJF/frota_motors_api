package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.enums.OrderStatus;
import com.example.frotamotors.domain.enums.OrderType;
import com.example.frotamotors.domain.model.Purchase;
import com.example.frotamotors.domain.service.PurchaseService;
import com.example.frotamotors.infrastructure.dto.PageResponseDTO;
import com.example.frotamotors.infrastructure.dto.PurchaseCreateDTO;
import com.example.frotamotors.infrastructure.dto.PurchaseResponseDTO;
import com.example.frotamotors.infrastructure.mapper.PurchaseMapper;
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
@RequestMapping("api/v1/purchases")
@RequiredArgsConstructor
@Slf4j
public class PurchaseController {

  @Autowired private PurchaseService purchaseService;

  @GetMapping("/search")
  public ResponseEntity<PageResponseDTO<PurchaseResponseDTO>> search(
      @RequestParam(required = false) UUID buyerId,
      @RequestParam(required = false) OrderStatus status,
      @RequestParam(required = false) OrderType type,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {

    Page<Purchase> page = purchaseService.search(buyerId, status, type, pageable);

    List<PurchaseResponseDTO> content =
        page.getContent().stream().map(PurchaseMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<PurchaseResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<PageResponseDTO<PurchaseResponseDTO>> getAll(
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Purchase> page = purchaseService.getAll(pageable);

    List<PurchaseResponseDTO> content =
        page.getContent().stream().map(PurchaseMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<PurchaseResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/buyer/{buyerId}")
  public ResponseEntity<PageResponseDTO<PurchaseResponseDTO>> getByBuyer(
      @PathVariable UUID buyerId,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Purchase> page = purchaseService.getByBuyer(buyerId, pageable);

    List<PurchaseResponseDTO> content =
        page.getContent().stream().map(PurchaseMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<PurchaseResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/status/{status}")
  public ResponseEntity<PageResponseDTO<PurchaseResponseDTO>> getByStatus(
      @PathVariable OrderStatus status,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Purchase> page = purchaseService.getByStatus(status, pageable);

    List<PurchaseResponseDTO> content =
        page.getContent().stream().map(PurchaseMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<PurchaseResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/type/{type}")
  public ResponseEntity<PageResponseDTO<PurchaseResponseDTO>> getByType(
      @PathVariable OrderType type,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Purchase> page = purchaseService.getByType(type, pageable);

    List<PurchaseResponseDTO> content =
        page.getContent().stream().map(PurchaseMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<PurchaseResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("{id}")
  public ResponseEntity<PurchaseResponseDTO> getById(@PathVariable UUID id) {
    Purchase purchase = purchaseService.getById(id);
    return ResponseEntity.ok(PurchaseMapper.toResponse(purchase));
  }

  @PostMapping
  public ResponseEntity<PurchaseResponseDTO> create(@RequestBody PurchaseCreateDTO dto) {
    Purchase saved = purchaseService.create(dto);
    return ResponseEntity.ok(PurchaseMapper.toResponse(saved));
  }

  @PutMapping("{id}/status")
  public ResponseEntity<PurchaseResponseDTO> updateStatus(
      @PathVariable UUID id,
      @RequestParam OrderStatus status,
      @RequestParam(required = false) String trackingNumber) {
    Purchase updated = purchaseService.updateStatus(id, status, trackingNumber);
    return ResponseEntity.ok(PurchaseMapper.toResponse(updated));
  }

  @PostMapping("{id}/confirm")
  public ResponseEntity<PurchaseResponseDTO> confirmOrder(@PathVariable UUID id) {
    Purchase purchase = purchaseService.confirmOrder(id);
    return ResponseEntity.ok(PurchaseMapper.toResponse(purchase));
  }

  @PostMapping("{id}/cancel")
  public ResponseEntity<PurchaseResponseDTO> cancelOrder(@PathVariable UUID id) {
    Purchase purchase = purchaseService.cancelOrder(id);
    return ResponseEntity.ok(PurchaseMapper.toResponse(purchase));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    purchaseService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
