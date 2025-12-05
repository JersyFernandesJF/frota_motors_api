package com.example.frotamotors.infrastructure.mapper;

import com.example.frotamotors.domain.enums.ComplaintStatus;
import com.example.frotamotors.domain.model.Agency;
import com.example.frotamotors.domain.model.Complaint;
import com.example.frotamotors.domain.model.Part;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.infrastructure.dto.ComplaintCreateDTO;
import com.example.frotamotors.infrastructure.dto.ComplaintResponseDTO;

public class ComplaintMapper {

  private ComplaintMapper() {}

  public static ComplaintResponseDTO toResponse(Complaint complaint) {
    return new ComplaintResponseDTO(
        complaint.getId(),
        complaint.getReporter() != null ? UserMapper.toResponse(complaint.getReporter()) : null,
        complaint.getType(),
        complaint.getStatus(),
        complaint.getDescription(),
<<<<<<< Updated upstream
        complaint.getReportedUser() != null
            ? UserMapper.toResponse(complaint.getReportedUser())
            : null,
        complaint.getReportedVehicle() != null
            ? VehicleMapper.toResponse(complaint.getReportedVehicle())
            : null,
        complaint.getReportedPart() != null
            ? PartMapper.toResponse(complaint.getReportedPart())
            : null,
        complaint.getReportedAgency() != null
            ? AgencyMapper.toResponse(complaint.getReportedAgency())
            : null,
        complaint.getReviewedBy() != null
            ? UserMapper.toResponse(complaint.getReviewedBy())
            : null,
=======
        complaint.getReportedUser() != null ? UserMapper.toResponse(complaint.getReportedUser()) : null,
        complaint.getReportedVehicle() != null ? VehicleMapper.toResponse(complaint.getReportedVehicle()) : null,
        complaint.getReportedPart() != null ? PartMapper.toResponse(complaint.getReportedPart()) : null,
        complaint.getReportedAgency() != null ? AgencyMapper.toResponse(complaint.getReportedAgency()) : null,
        complaint.getReviewedBy() != null ? UserMapper.toResponse(complaint.getReviewedBy()) : null,
>>>>>>> Stashed changes
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
      Agency reportedAgency) {
    Complaint complaint = new Complaint();
    complaint.setReporter(reporter);
    complaint.setType(dto.type());
    complaint.setStatus(ComplaintStatus.PENDING);
    complaint.setDescription(dto.description());
    complaint.setReportedUser(reportedUser);
    complaint.setReportedVehicle(reportedVehicle);
    complaint.setReportedPart(reportedPart);
    complaint.setReportedAgency(reportedAgency);
    return complaint;
  }
}
