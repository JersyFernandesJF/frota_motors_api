package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.enums.FinancingStatus;
import com.example.frotamotors.domain.model.Financing;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.infrastructure.dto.CreditScoreSimulationDTO;
import com.example.frotamotors.infrastructure.dto.FinancingCreateDTO;
import com.example.frotamotors.infrastructure.dto.FinancingRejectRequestDTO;
import com.example.frotamotors.infrastructure.mapper.FinancingMapper;
import com.example.frotamotors.infrastructure.persistence.FinancingRepository;
import com.example.frotamotors.infrastructure.persistence.UserRepository;
import com.example.frotamotors.infrastructure.persistence.VehicleRepository;
import com.example.frotamotors.infrastructure.util.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FinancingService {

  @Autowired private FinancingRepository financingRepository;

  @Autowired private VehicleRepository vehicleRepository;

  @Autowired private UserRepository userRepository;

  @Transactional
  public Financing create(FinancingCreateDTO dto) {
    Vehicle vehicle =
        vehicleRepository
            .findById(dto.vehicleId())
            .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));

    User buyer =
        userRepository
            .findById(dto.buyerId())
            .orElseThrow(() -> new EntityNotFoundException("Buyer not found"));

    User seller =
        userRepository
            .findById(dto.sellerId())
            .orElseThrow(() -> new EntityNotFoundException("Seller not found"));

    Financing financing = FinancingMapper.toEntity(dto, vehicle, buyer, seller);
    return financingRepository.save(financing);
  }

  public Financing getById(UUID id) {
    return financingRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Financing not found"));
  }

  public Page<Financing> getAll(Pageable pageable) {
    return financingRepository.findAll(pageable);
  }

  public Page<Financing> search(
      UUID buyerId,
      UUID sellerId,
      UUID vehicleId,
      FinancingStatus status,
      Pageable pageable) {
    return financingRepository.searchPageable(buyerId, sellerId, vehicleId, status, pageable);
  }

  @Transactional
  public Financing approve(UUID id) {
    Financing financing = getById(id);
    UUID adminId = SecurityUtils.getCurrentUserId();
    User admin = userRepository.findById(adminId).orElse(null);

    financing.setStatus(FinancingStatus.APPROVED);
    financing.setApprovedBy(admin);
    financing.setApprovedAt(LocalDateTime.now());
    financing.setRejectedBy(null);
    financing.setRejectedAt(null);
    financing.setRejectionReason(null);

    return financingRepository.save(financing);
  }

  @Transactional
  public Financing reject(UUID id, FinancingRejectRequestDTO request) {
    Financing financing = getById(id);
    UUID adminId = SecurityUtils.getCurrentUserId();
    User admin = userRepository.findById(adminId).orElse(null);

    financing.setStatus(FinancingStatus.REJECTED);
    financing.setRejectedBy(admin);
    financing.setRejectedAt(LocalDateTime.now());
    financing.setRejectionReason(request.reason());
    financing.setApprovedBy(null);
    financing.setApprovedAt(null);

    return financingRepository.save(financing);
  }

  public Integer simulateCreditScore(CreditScoreSimulationDTO request) {
    // Simple credit score simulation algorithm
    // This is a simplified version - in production, use a proper credit scoring service
    int score = 650; // Base score

    // Income to expenses ratio
    if (request.monthlyIncome().compareTo(BigDecimal.ZERO) > 0) {
      BigDecimal ratio =
          request.monthlyExpenses().divide(
              request.monthlyIncome(), 2, java.math.RoundingMode.HALF_UP);
      if (ratio.compareTo(BigDecimal.valueOf(0.3)) < 0) {
        score += 50; // Low debt-to-income ratio
      } else if (ratio.compareTo(BigDecimal.valueOf(0.5)) < 0) {
        score += 25; // Moderate debt-to-income ratio
      } else {
        score -= 25; // High debt-to-income ratio
      }
    }

    // Existing loans
    if (request.existingLoans() == 0) {
      score += 30; // No existing loans
    } else if (request.existingLoans() <= 2) {
      score += 10; // Few loans
    } else {
      score -= 20; // Many loans
    }

    // Credit history
    if (request.creditHistoryMonths() >= 60) {
      score += 40; // Long credit history
    } else if (request.creditHistoryMonths() >= 24) {
      score += 20; // Medium credit history
    } else if (request.creditHistoryMonths() >= 12) {
      score += 10; // Short credit history
    } else {
      score -= 30; // Very short or no credit history
    }

    // Cap score between 300 and 850
    score = Math.max(300, Math.min(850, score));

    return score;
  }

  @Transactional
  public Financing updateCreditScore(UUID id, Integer creditScore) {
    Financing financing = getById(id);
    financing.setCreditScore(creditScore);
    return financingRepository.save(financing);
  }

  @Transactional
  public void delete(UUID id) {
    if (!financingRepository.existsById(id)) {
      throw new EntityNotFoundException("Financing not found");
    }
    financingRepository.deleteById(id);
  }
}

