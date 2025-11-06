package com.example.kubico.domain.service;

import com.example.kubico.domain.enums.ComplaintStatus;
import com.example.kubico.domain.enums.ComplaintType;
import com.example.kubico.domain.model.Agency;
import com.example.kubico.domain.model.Complaint;
import com.example.kubico.domain.model.Part;
import com.example.kubico.domain.model.Property;
import com.example.kubico.domain.model.User;
import com.example.kubico.domain.model.Vehicle;
import com.example.kubico.infrastructure.dto.ComplaintCreateDTO;
import com.example.kubico.infrastructure.mapper.ComplaintMapper;
import com.example.kubico.infrastructure.persistence.AgencyRepository;
import com.example.kubico.infrastructure.persistence.ComplaintRepository;
import com.example.kubico.infrastructure.persistence.PartRepository;
import com.example.kubico.infrastructure.persistence.PropertyRepository;
import com.example.kubico.infrastructure.persistence.UserRepository;
import com.example.kubico.infrastructure.persistence.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ComplaintService {

  @Autowired private ComplaintRepository complaintRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private VehicleRepository vehicleRepository;

  @Autowired private PartRepository partRepository;

  @Autowired private PropertyRepository propertyRepository;

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
    Property reportedProperty = null;
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
    if (dto.reportedPropertyId() != null) {
      reportedProperty =
          propertyRepository
              .findById(dto.reportedPropertyId())
              .orElseThrow(() -> new EntityNotFoundException("Reported property not found"));
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
            dto, reporter, reportedUser, reportedVehicle, reportedPart, reportedProperty,
            reportedAgency);
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

  public List<Complaint> search(ComplaintStatus status, ComplaintType type, UUID reporterId) {
    return complaintRepository.search(status, type, reporterId);
  }

  public Page<Complaint> search(
      ComplaintStatus status, ComplaintType type, UUID reporterId, Pageable pageable) {
    return complaintRepository.searchPageable(status, type, reporterId, pageable);
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

  public List<Complaint> getByReportedProperty(UUID reportedPropertyId) {
    return complaintRepository.findByReportedPropertyId(reportedPropertyId);
  }

  public Page<Complaint> getByReportedProperty(UUID reportedPropertyId, Pageable pageable) {
    return complaintRepository.findByReportedPropertyId(reportedPropertyId, pageable);
  }

  public List<Complaint> getByReportedAgency(UUID reportedAgencyId) {
    return complaintRepository.findByReportedAgencyId(reportedAgencyId);
  }

  public Page<Complaint> getByReportedAgency(UUID reportedAgencyId, Pageable pageable) {
    return complaintRepository.findByReportedAgencyId(reportedAgencyId, pageable);
  }
}

