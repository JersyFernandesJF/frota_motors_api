package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.enums.ComplaintPriority;
import com.example.frotamotors.domain.enums.ComplaintStatus;
import com.example.frotamotors.domain.enums.ComplaintType;
import com.example.frotamotors.domain.model.Agency;
import com.example.frotamotors.domain.model.Complaint;
import com.example.frotamotors.domain.model.Part;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.infrastructure.dto.ComplaintCreateDTO;
import com.example.frotamotors.infrastructure.dto.ComplaintDismissRequestDTO;
import com.example.frotamotors.infrastructure.dto.ComplaintResolveRequestDTO;
import com.example.frotamotors.infrastructure.dto.ExportRequestDTO;
import com.example.frotamotors.infrastructure.mapper.ComplaintMapper;
import com.example.frotamotors.infrastructure.persistence.AgencyRepository;
import com.example.frotamotors.infrastructure.persistence.ComplaintRepository;
import com.example.frotamotors.infrastructure.persistence.PartRepository;
import com.example.frotamotors.infrastructure.persistence.UserRepository;
import com.example.frotamotors.infrastructure.persistence.VehicleRepository;
import com.example.frotamotors.infrastructure.util.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComplaintService {

  @Autowired private ComplaintRepository complaintRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private VehicleRepository vehicleRepository;

  @Autowired private PartRepository partRepository;

  @Autowired private AgencyRepository agencyRepository;

  public Complaint create(ComplaintCreateDTO dto) {
    User reporter =
        userRepository
            .findById(dto.reporterId())
            .orElseThrow(() -> new EntityNotFoundException("Reporter not found"));

    // Validate that exactly one reported entity is provided
    int reportedCount = 0;
    User reportedUser = null;
    Vehicle reportedVehicle = null;
    Part reportedPart = null;
    Agency reportedAgency = null;

    if (dto.reportedUserId() != null) {
      reportedUser =
          userRepository
              .findById(dto.reportedUserId())
              .orElseThrow(() -> new EntityNotFoundException("Reported user not found"));
      reportedCount++;
    }
    if (dto.reportedVehicleId() != null) {
      reportedVehicle =
          vehicleRepository
              .findById(dto.reportedVehicleId())
              .orElseThrow(() -> new EntityNotFoundException("Reported vehicle not found"));
      reportedCount++;
    }
    if (dto.reportedPartId() != null) {
      reportedPart =
          partRepository
              .findById(dto.reportedPartId())
              .orElseThrow(() -> new EntityNotFoundException("Reported part not found"));
      reportedCount++;
    }
    if (dto.reportedAgencyId() != null) {
      reportedAgency =
          agencyRepository
              .findById(dto.reportedAgencyId())
              .orElseThrow(() -> new EntityNotFoundException("Reported agency not found"));
      reportedCount++;
    }

    if (reportedCount != 1) {
      throw new IllegalArgumentException("Exactly one reported entity must be provided");
    }

    Complaint complaint =
        ComplaintMapper.toEntity(
            dto, reporter, reportedUser, reportedVehicle, reportedPart, reportedAgency);
    return complaintRepository.save(complaint);
  }

  public List<Complaint> getAll() {
    return complaintRepository.findAll();
  }

  public Page<Complaint> getAll(Pageable pageable) {
    return complaintRepository.findAll(pageable);
  }

  public Complaint getById(UUID id) {
    return complaintRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Complaint not found"));
  }

  public void delete(UUID id) {
    if (!complaintRepository.existsById(id)) {
      throw new EntityNotFoundException("Complaint not found");
    }
    complaintRepository.deleteById(id);
  }

  public Complaint updateStatus(UUID id, ComplaintStatus status, UUID reviewedById, String notes) {
    Complaint complaint = getById(id);
    complaint.setStatus(status);
    if (reviewedById != null) {
      User reviewer =
          userRepository
              .findById(reviewedById)
              .orElseThrow(() -> new EntityNotFoundException("Reviewer not found"));
      complaint.setReviewedBy(reviewer);
    }
    if (notes != null) {
      complaint.setAdminNotes(notes);
    }
    return complaintRepository.save(complaint);
  }

  public List<Complaint> search(
      ComplaintStatus status, ComplaintType type, UUID reporterId, UUID reportedVehicleId) {
    return complaintRepository.search(status, type, reporterId, reportedVehicleId);
  }

  public Page<Complaint> search(
      ComplaintStatus status,
      ComplaintPriority priority,
      ComplaintType type,
      UUID reporterId,
      UUID reportedVehicleId,
      String reason,
      Pageable pageable) {
    return complaintRepository.searchPageable(
        status, priority, type, reporterId, reportedVehicleId, reason, pageable);
  }

  public List<Complaint> getByReporter(UUID reporterId) {
    return complaintRepository.findByReporterId(reporterId);
  }

  public Page<Complaint> getByReporter(UUID reporterId, Pageable pageable) {
    return complaintRepository.findByReporterId(reporterId, pageable);
  }

  public List<Complaint> getByStatus(ComplaintStatus status) {
    return complaintRepository.findByStatus(status);
  }

  public Page<Complaint> getByStatus(ComplaintStatus status, Pageable pageable) {
    return complaintRepository.findByStatus(status, pageable);
  }

  public List<Complaint> getByType(ComplaintType type) {
    return complaintRepository.findByType(type);
  }

  public Page<Complaint> getByType(ComplaintType type, Pageable pageable) {
    return complaintRepository.findByType(type, pageable);
  }

  public List<Complaint> getByReportedUser(UUID reportedUserId) {
    return complaintRepository.findByReportedUserId(reportedUserId);
  }

  public Page<Complaint> getByReportedUser(UUID reportedUserId, Pageable pageable) {
    return complaintRepository.findByReportedUserId(reportedUserId, pageable);
  }

  public List<Complaint> getByReportedVehicle(UUID reportedVehicleId) {
    return complaintRepository.findByReportedVehicleId(reportedVehicleId);
  }

  public Page<Complaint> getByReportedVehicle(UUID reportedVehicleId, Pageable pageable) {
    return complaintRepository.findByReportedVehicleId(reportedVehicleId, pageable);
  }

  public List<Complaint> getByReportedPart(UUID reportedPartId) {
    return complaintRepository.findByReportedPartId(reportedPartId);
  }

  public Page<Complaint> getByReportedPart(UUID reportedPartId, Pageable pageable) {
    return complaintRepository.findByReportedPartId(reportedPartId, pageable);
  }

  public List<Complaint> getByReportedAgency(UUID reportedAgencyId) {
    return complaintRepository.findByReportedAgencyId(reportedAgencyId);
  }

  public Page<Complaint> getByReportedAgency(UUID reportedAgencyId, Pageable pageable) {
    return complaintRepository.findByReportedAgencyId(reportedAgencyId, pageable);
  }

  @Transactional
  public Complaint updatePriority(UUID id, ComplaintPriority priority) {
    Complaint complaint = getById(id);
    complaint.setPriority(priority);
    return complaintRepository.save(complaint);
  }

  @Transactional
  public Complaint resolveComplaint(UUID id, ComplaintResolveRequestDTO request) {
    Complaint complaint = getById(id);
    UUID adminId = SecurityUtils.getCurrentUserId();
    User admin = userRepository.findById(adminId).orElse(null);

    complaint.setStatus(ComplaintStatus.RESOLVED);
    complaint.setResolvedBy(admin);
    complaint.setResolvedAt(LocalDateTime.now());
    complaint.setResolution(request.notes());

    Complaint saved = complaintRepository.save(complaint);

    // TODO: Notify reporter and seller if requested
    if (request.notifyReporter() != null && request.notifyReporter()) {
      // Implement notification
    }
    if (request.notifySeller() != null && request.notifySeller()) {
      // Implement notification
    }

    return saved;
  }

  @Transactional
  public Complaint dismissComplaint(UUID id, ComplaintDismissRequestDTO request) {
    Complaint complaint = getById(id);
    UUID adminId = SecurityUtils.getCurrentUserId();
    User admin = userRepository.findById(adminId).orElse(null);

    complaint.setStatus(ComplaintStatus.DISMISSED);
    complaint.setDismissedBy(admin);
    complaint.setDismissedAt(LocalDateTime.now());
    complaint.setResolution(request.reason());

    Complaint saved = complaintRepository.save(complaint);

    // TODO: Notify reporter if requested
    if (request.notifyReporter() != null && request.notifyReporter()) {
      // Implement notification
    }

    return saved;
  }

  public Map<String, Object> getStatistics(String period) {
    Map<String, Object> stats = new HashMap<>();

    // Statistics by reason (type)
    Map<String, Long> byReason = new HashMap<>();
    for (ComplaintType type : ComplaintType.values()) {
      Long count = complaintRepository.countByType(type);
      byReason.put(type.name(), count);
    }
    stats.put("byReason", byReason);

    // Statistics by status
    Map<String, Long> byStatus = new HashMap<>();
    for (ComplaintStatus status : ComplaintStatus.values()) {
      Long count = complaintRepository.countByStatus(status);
      byStatus.put(status.name(), count);
    }
    stats.put("byStatus", byStatus);

    // Statistics by priority
    Map<String, Long> byPriority = new HashMap<>();
    for (ComplaintPriority priority : ComplaintPriority.values()) {
      Long count = complaintRepository.countByPriority(priority);
      byPriority.put(priority.name(), count);
    }
    stats.put("byPriority", byPriority);

    // Response time (average time from creation to resolution)
    Double avgResponseTime = complaintRepository.getAverageResponseTime(ComplaintStatus.RESOLVED);
    stats.put(
        "responseTime",
        Map.of("average", avgResponseTime != null ? avgResponseTime : 0.0, "unit", "hours"));

    return stats;
  }

  public String exportComplaints(ExportRequestDTO request) {
    // TODO: Implement export via ExportService
    return "Export functionality will be implemented with ExportService";
  }
}
