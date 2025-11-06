package com.example.kubico.infrastructure.mapper;

import com.example.kubico.domain.enums.ComplaintStatus;
import com.example.kubico.domain.model.Agency;
import com.example.kubico.domain.model.Complaint;
import com.example.kubico.domain.model.Part;
import com.example.kubico.domain.model.Property;
import com.example.kubico.domain.model.User;
import com.example.kubico.domain.model.Vehicle;
import com.example.kubico.infrastructure.dto.ComplaintCreateDTO;
import com.example.kubico.infrastructure.dto.ComplaintResponseDTO;

public class ComplaintMapper {

  private ComplaintMapper() {}

  public static ComplaintResponseDTO toResponse(Complaint complaint) {
    return new ComplaintResponseDTO(
        complaint.getId(),
        complaint.getReporter(),
        complaint.getType(),
        complaint.getStatus(),
        complaint.getDescription(),
        complaint.getReportedUser(),
        complaint.getReportedVehicle(),
        complaint.getReportedPart(),
        complaint.getReportedProperty(),
        complaint.getReportedAgency(),
        complaint.getReviewedBy(),
        complaint.getAdminNotes(),
        complaint.getCreatedAt(),
        complaint.getUpdatedAt());
  }

  public static Complaint toEntity(
      ComplaintCreateDTO dto,
      User reporter,
      User reportedUser,
      Vehicle reportedVehicle,
      Part reportedPart,
      Property reportedProperty,
      Agency reportedAgency) {
    Complaint complaint = new Complaint();
    complaint.setReporter(reporter);
    complaint.setType(dto.type());
    complaint.setStatus(ComplaintStatus.PENDING);
    complaint.setDescription(dto.description());
    complaint.setReportedUser(reportedUser);
    complaint.setReportedVehicle(reportedVehicle);
    complaint.setReportedPart(reportedPart);
    complaint.setReportedProperty(reportedProperty);
    complaint.setReportedAgency(reportedAgency);
    return complaint;
  }
}

