package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.enums.InspectionStatus;
import com.example.frotamotors.domain.model.Inspection;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.infrastructure.dto.InspectionCancelRequestDTO;
import com.example.frotamotors.infrastructure.dto.InspectionCreateDTO;
import com.example.frotamotors.infrastructure.dto.InspectionReportUploadDTO;
import com.example.frotamotors.infrastructure.dto.InspectionRescheduleRequestDTO;
import com.example.frotamotors.infrastructure.dto.InspectionUpdateDTO;
import com.example.frotamotors.infrastructure.mapper.InspectionMapper;
import com.example.frotamotors.infrastructure.persistence.InspectionRepository;
import com.example.frotamotors.infrastructure.persistence.UserRepository;
import com.example.frotamotors.infrastructure.persistence.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InspectionService {

  @Autowired private InspectionRepository inspectionRepository;

  @Autowired private VehicleRepository vehicleRepository;

  @Autowired private UserRepository userRepository;

  @Transactional
  public Inspection create(InspectionCreateDTO dto) {
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

    Inspection inspection = InspectionMapper.toEntity(dto, vehicle, buyer, seller);
    return inspectionRepository.save(inspection);
  }

  @Transactional(readOnly = true)
  public Inspection getById(UUID id) {
    Inspection inspection = inspectionRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Inspection not found"));
    // Initialize lazy relationships
    if (inspection.getVehicle() != null) {
      inspection.getVehicle().getId();
    }
    if (inspection.getBuyer() != null) {
      inspection.getBuyer().getId();
    }
    if (inspection.getSeller() != null) {
      inspection.getSeller().getId();
    }
    if (inspection.getInspector() != null) {
      inspection.getInspector().getId();
    }
    return inspection;
  }

  public Page<Inspection> getAll(Pageable pageable) {
    return inspectionRepository.findAll(pageable);
  }

  public Page<Inspection> search(
      UUID buyerId,
      UUID sellerId,
      UUID inspectorId,
      UUID vehicleId,
      InspectionStatus status,
      LocalDateTime startDate,
      LocalDateTime endDate,
      Pageable pageable) {
    return inspectionRepository.searchPageable(
        buyerId, sellerId, inspectorId, vehicleId, status, startDate, endDate, pageable);
  }

  @Transactional
  public Inspection update(UUID id, InspectionUpdateDTO dto) {
    Inspection inspection = getById(id);

    if (dto.scheduledAt() != null) {
      inspection.setScheduledAt(dto.scheduledAt());
    }
    if (dto.location() != null) {
      inspection.setLocation(dto.location());
    }
    if (dto.notes() != null) {
      inspection.setNotes(dto.notes());
    }
    if (dto.inspectorId() != null) {
      User inspector =
          userRepository
              .findById(dto.inspectorId())
              .orElseThrow(() -> new EntityNotFoundException("Inspector not found"));
      inspection.setInspector(inspector);
    }
    if (dto.status() != null) {
      // If status is explicitly set to CANCELLED, reuse cancel logic
      if (dto.status() == InspectionStatus.CANCELLED) {
        InspectionCancelRequestDTO request =
            new InspectionCancelRequestDTO(dto.notes() != null ? dto.notes() : "Cancelled");
        return cancel(id, request);
      }
      inspection.setStatus(dto.status());
    }

    return inspectionRepository.save(inspection);
  }

  @Transactional
  public Inspection confirm(UUID id) {
    Inspection inspection = getById(id);
    inspection.setStatus(InspectionStatus.CONFIRMED);
    inspection.setConfirmedAt(LocalDateTime.now());
    return inspectionRepository.save(inspection);
  }

  @Transactional
  public Inspection reschedule(UUID id, InspectionRescheduleRequestDTO request) {
    Inspection inspection = getById(id);
    inspection.setStatus(InspectionStatus.RESCHEDULED);
    inspection.setScheduledAt(request.newScheduledAt());
    if (request.reason() != null) {
      inspection.setNotes(
          (inspection.getNotes() != null ? inspection.getNotes() + "\n" : "")
              + "Rescheduled: "
              + request.reason());
    }
    return inspectionRepository.save(inspection);
  }

  @Transactional
  public Inspection cancel(UUID id, InspectionCancelRequestDTO request) {
    Inspection inspection = getById(id);
    inspection.setStatus(InspectionStatus.CANCELLED);
    inspection.setCancelledAt(LocalDateTime.now());
    inspection.setCancellationReason(request.reason());
    return inspectionRepository.save(inspection);
  }

  @Transactional
  public Inspection assignInspector(UUID id, UUID inspectorId) {
    Inspection inspection = getById(id);
    User inspector =
        userRepository
            .findById(inspectorId)
            .orElseThrow(() -> new EntityNotFoundException("Inspector not found"));
    inspection.setInspector(inspector);
    if (inspection.getStatus() == InspectionStatus.PENDING) {
      inspection.setStatus(InspectionStatus.SCHEDULED);
    }
    return inspectionRepository.save(inspection);
  }

  @Transactional
  public Inspection uploadReport(UUID id, InspectionReportUploadDTO request) {
    Inspection inspection = getById(id);
    inspection.setReportUrl(request.reportUrl());
    inspection.setStatus(InspectionStatus.COMPLETED);
    inspection.setCompletedAt(LocalDateTime.now());
    return inspectionRepository.save(inspection);
  }

  @Transactional
  public void delete(UUID id) {
    if (!inspectionRepository.existsById(id)) {
      throw new EntityNotFoundException("Inspection not found");
    }
    inspectionRepository.deleteById(id);
  }

  @Transactional
  public void sendReminder(UUID id) {
    Inspection inspection = getById(id);
    
    // TODO: Implement email/notification sending
    // This should send reminders to buyer, seller, and inspector (if assigned)
    // For now, we'll just log it
    log.info(
        "Sending reminder for inspection {} - Vehicle: {}, Buyer: {}, Seller: {}, Scheduled: {}",
        inspection.getId(),
        inspection.getVehicle() != null ? inspection.getVehicle().getId() : "N/A",
        inspection.getBuyer() != null ? inspection.getBuyer().getEmail() : "N/A",
        inspection.getSeller() != null ? inspection.getSeller().getEmail() : "N/A",
        inspection.getScheduledAt());
    
    // In a real implementation, you would:
    // 1. Send email to buyer
    // 2. Send email to seller
    // 3. Send email to inspector (if assigned)
    // 4. Optionally send push notifications
  }
}
