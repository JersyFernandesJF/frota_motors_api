package com.example.frotamotors.infrastructure.mapper;

import com.example.frotamotors.domain.enums.ComplaintStatus;
import com.example.frotamotors.domain.model.Agency;
import com.example.frotamotors.domain.model.Complaint;
import com.example.frotamotors.domain.model.Part;
import com.example.frotamotors.domain.model.Property;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.infrastructure.dto.ComplaintCreateDTO;
import com.example.frotamotors.infrastructure.dto.ComplaintResponseDTO;

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
