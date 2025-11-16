package com.example.frotamotors.infrastructure.mapper;

import com.example.frotamotors.domain.model.Inspection;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.infrastructure.dto.InspectionCreateDTO;
import com.example.frotamotors.infrastructure.dto.InspectionResponseDTO;

public class InspectionMapper {

  private InspectionMapper() {}

  public static InspectionResponseDTO toResponse(Inspection inspection) {
    return new InspectionResponseDTO(
        inspection.getId(),
        inspection.getVehicle().getId(),
        inspection.getBuyer().getId(),
        inspection.getSeller().getId(),
        inspection.getInspector() != null ? inspection.getInspector().getId() : null,
        inspection.getStatus(),
        inspection.getScheduledAt(),
        inspection.getLocation(),
        inspection.getNotes(),
        inspection.getReportUrl(),
        inspection.getConfirmedAt(),
        inspection.getCompletedAt(),
        inspection.getCancelledAt(),
        inspection.getCancellationReason(),
        inspection.getCreatedAt(),
        inspection.getUpdatedAt());
  }

  public static Inspection toEntity(
      InspectionCreateDTO dto, Vehicle vehicle, User buyer, User seller) {
    Inspection inspection = new Inspection();
    inspection.setVehicle(vehicle);
    inspection.setBuyer(buyer);
    inspection.setSeller(seller);
    inspection.setScheduledAt(dto.scheduledAt());
    inspection.setLocation(dto.location());
    inspection.setNotes(dto.notes());
    return inspection;
  }
}
