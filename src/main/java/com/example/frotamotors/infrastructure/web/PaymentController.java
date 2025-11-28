package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.enums.PaymentMethod;
import com.example.frotamotors.domain.enums.PaymentStatus;
import com.example.frotamotors.domain.model.Payment;
import com.example.frotamotors.domain.service.PaymentService;
import com.example.frotamotors.infrastructure.dto.PageResponseDTO;
import com.example.frotamotors.infrastructure.dto.PaymentCreateDTO;
import com.example.frotamotors.infrastructure.dto.PaymentResponseDTO;
import com.example.frotamotors.infrastructure.mapper.PaymentMapper;
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
@RequestMapping("api/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

  @Autowired private PaymentService paymentService;

  @GetMapping("/search")
  public ResponseEntity<PageResponseDTO<PaymentResponseDTO>> search(
      @RequestParam(required = false) UUID payerId,
      @RequestParam(required = false) PaymentStatus status,
      @RequestParam(required = false) PaymentMethod method,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {

    Page<Payment> page = paymentService.search(payerId, status, method, pageable);

    List<PaymentResponseDTO> content =
        page.getContent().stream().map(PaymentMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<PaymentResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<PageResponseDTO<PaymentResponseDTO>> getAll(
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Payment> page = paymentService.getAll(pageable);

    List<PaymentResponseDTO> content =
        page.getContent().stream().map(PaymentMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<PaymentResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/payer/{payerId}")
  public ResponseEntity<PageResponseDTO<PaymentResponseDTO>> getByPayer(
      @PathVariable UUID payerId,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Payment> page = paymentService.getByPayer(payerId, pageable);

    List<PaymentResponseDTO> content =
        page.getContent().stream().map(PaymentMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<PaymentResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/purchase/{purchaseId}")
  public ResponseEntity<PageResponseDTO<PaymentResponseDTO>> getByPurchase(
      @PathVariable UUID purchaseId,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Payment> page = paymentService.getByPurchase(purchaseId, pageable);

    List<PaymentResponseDTO> content =
        page.getContent().stream().map(PaymentMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<PaymentResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/status/{status}")
  public ResponseEntity<PageResponseDTO<PaymentResponseDTO>> getByStatus(
      @PathVariable PaymentStatus status,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Payment> page = paymentService.getByStatus(status, pageable);

    List<PaymentResponseDTO> content =
        page.getContent().stream().map(PaymentMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<PaymentResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("{id}")
  public ResponseEntity<PaymentResponseDTO> getById(@PathVariable UUID id) {
    Payment payment = paymentService.getById(id);
    return ResponseEntity.ok(PaymentMapper.toResponse(payment));
  }

  @PostMapping
  public ResponseEntity<PaymentResponseDTO> create(@RequestBody PaymentCreateDTO dto) {
    Payment saved = paymentService.create(dto);
    return ResponseEntity.ok(PaymentMapper.toResponse(saved));
  }

  @PutMapping("{id}/status")
  public ResponseEntity<PaymentResponseDTO> updateStatus(
      @PathVariable UUID id,
      @RequestParam PaymentStatus status,
      @RequestParam(required = false) String transactionId,
      @RequestParam(required = false) String failureReason) {
    Payment updated = paymentService.updateStatus(id, status, transactionId, failureReason);
    return ResponseEntity.ok(PaymentMapper.toResponse(updated));
  }

  @PostMapping("{id}/process")
  public ResponseEntity<PaymentResponseDTO> processPayment(@PathVariable UUID id) {
    Payment payment = paymentService.processPayment(id);
    return ResponseEntity.ok(PaymentMapper.toResponse(payment));
  }

  @PostMapping("{id}/complete")
  public ResponseEntity<PaymentResponseDTO> completePayment(
      @PathVariable UUID id, @RequestParam(required = false) String transactionId) {
    Payment payment = paymentService.completePayment(id, transactionId);
    return ResponseEntity.ok(PaymentMapper.toResponse(payment));
  }

  @PostMapping("{id}/fail")
  public ResponseEntity<PaymentResponseDTO> failPayment(
      @PathVariable UUID id, @RequestParam String failureReason) {
    Payment payment = paymentService.failPayment(id, failureReason);
    return ResponseEntity.ok(PaymentMapper.toResponse(payment));
  }

  @PostMapping("{id}/refund")
  public ResponseEntity<PaymentResponseDTO> refundPayment(
      @PathVariable UUID id, @RequestParam(required = false) String notes) {
    Payment payment = paymentService.refundPayment(id, notes);
    return ResponseEntity.ok(PaymentMapper.toResponse(payment));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    paymentService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
