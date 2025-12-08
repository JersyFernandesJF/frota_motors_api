package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.model.Subscription;
import com.example.frotamotors.domain.service.SubscriptionService;
import com.example.frotamotors.infrastructure.dto.SubscriptionCreateDTO;
import com.example.frotamotors.infrastructure.dto.SubscriptionResponseDTO;
import com.example.frotamotors.infrastructure.mapper.SubscriptionMapper;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/subscriptions")
@RequiredArgsConstructor
@Slf4j
public class SubscriptionController {

  private final SubscriptionService subscriptionService;

  @GetMapping
  public ResponseEntity<List<SubscriptionResponseDTO>> getAll() {
    List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
    List<SubscriptionResponseDTO> response =
        subscriptions.stream().map(SubscriptionMapper::toResponse).collect(Collectors.toList());
    return ResponseEntity.ok(response);
  }

  @GetMapping("{id}")
  public ResponseEntity<SubscriptionResponseDTO> getById(@PathVariable UUID id) {
    Subscription subscription = subscriptionService.getSubscriptionById(id);
    return ResponseEntity.ok(SubscriptionMapper.toResponse(subscription));
  }

  @GetMapping("agency/{agencyId}")
  public ResponseEntity<SubscriptionResponseDTO> getByAgencyId(@PathVariable UUID agencyId) {
    Subscription subscription = subscriptionService.getSubscriptionByAgencyId(agencyId);
    return ResponseEntity.ok(SubscriptionMapper.toResponse(subscription));
  }

  @PostMapping
  public ResponseEntity<SubscriptionResponseDTO> create(@RequestBody SubscriptionCreateDTO dto) {
    Subscription subscription = subscriptionService.createSubscription(dto);
    return ResponseEntity.ok(SubscriptionMapper.toResponse(subscription));
  }

  @PutMapping("{id}/renew")
  public ResponseEntity<SubscriptionResponseDTO> renew(@PathVariable UUID id) {
    Subscription subscription = subscriptionService.renewSubscription(id);
    return ResponseEntity.ok(SubscriptionMapper.toResponse(subscription));
  }

  @PutMapping("{id}/cancel")
  public ResponseEntity<SubscriptionResponseDTO> cancel(@PathVariable UUID id) {
    Subscription subscription = subscriptionService.cancelSubscription(id);
    return ResponseEntity.ok(SubscriptionMapper.toResponse(subscription));
  }

  @PutMapping("{id}/suspend")
  public ResponseEntity<SubscriptionResponseDTO> suspend(@PathVariable UUID id) {
    Subscription subscription = subscriptionService.suspendSubscription(id);
    return ResponseEntity.ok(SubscriptionMapper.toResponse(subscription));
  }
}
