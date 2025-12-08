package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.model.SubscriptionPayment;
import com.example.frotamotors.domain.service.SubscriptionPaymentService;
import com.example.frotamotors.infrastructure.dto.SubscriptionPaymentCreateDTO;
import com.example.frotamotors.infrastructure.dto.SubscriptionPaymentResponseDTO;
import com.example.frotamotors.infrastructure.mapper.SubscriptionPaymentMapper;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/subscription-payments")
@RequiredArgsConstructor
@Slf4j
public class SubscriptionPaymentController {

  private final SubscriptionPaymentService paymentService;

  @GetMapping("{id}")
  public ResponseEntity<SubscriptionPaymentResponseDTO> getById(@PathVariable UUID id) {
    SubscriptionPayment payment = paymentService.getPaymentById(id);
    return ResponseEntity.ok(SubscriptionPaymentMapper.toResponse(payment));
  }

  @GetMapping("subscription/{subscriptionId}")
  public ResponseEntity<List<SubscriptionPaymentResponseDTO>> getBySubscriptionId(
      @PathVariable UUID subscriptionId) {
    List<SubscriptionPayment> payments = paymentService.getPaymentsBySubscriptionId(subscriptionId);
    List<SubscriptionPaymentResponseDTO> response =
        payments.stream().map(SubscriptionPaymentMapper::toResponse).collect(Collectors.toList());
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<SubscriptionPaymentResponseDTO> create(
      @RequestBody SubscriptionPaymentCreateDTO dto) {
    SubscriptionPayment payment = paymentService.createPayment(dto);
    return ResponseEntity.ok(SubscriptionPaymentMapper.toResponse(payment));
  }
}
