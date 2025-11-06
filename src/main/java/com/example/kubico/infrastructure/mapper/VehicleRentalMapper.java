package com.example.kubico.infrastructure.mapper;

import com.example.kubico.domain.enums.RentalStatus;
import com.example.kubico.domain.model.Agency;
import com.example.kubico.domain.model.User;
import com.example.kubico.domain.model.Vehicle;
import com.example.kubico.domain.model.VehicleRental;
import com.example.kubico.infrastructure.dto.VehicleRentalCreateDTO;
import com.example.kubico.infrastructure.dto.VehicleRentalResponseDTO;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

public class VehicleRentalMapper {

  private VehicleRentalMapper() {}

  public static VehicleRentalResponseDTO toResponse(VehicleRental rental) {
    return new VehicleRentalResponseDTO(
        rental.getId(),
        rental.getVehicle(),
        rental.getRenter(),
        rental.getAgency(),
        rental.getStatus(),
        rental.getStartDate(),
        rental.getEndDate(),
        rental.getActualReturnDate(),
        rental.getDailyRate(),
        rental.getCurrency(),
        rental.getTotalAmount(),
        rental.getDepositAmount(),
        rental.getDepositReturned(),
        rental.getNotes(),
        rental.getPickupLocation(),
        rental.getReturnLocation(),
        rental.getMileageAtPickup(),
        rental.getMileageAtReturn(),
        rental.getCreatedAt(),
        rental.getUpdatedAt());
  }

  public static VehicleRental toEntity(
      VehicleRentalCreateDTO dto, Vehicle vehicle, User renter, Agency agency) {
    VehicleRental rental = new VehicleRental();
    rental.setVehicle(vehicle);
    rental.setRenter(renter);
    rental.setAgency(agency);
    rental.setStatus(RentalStatus.PENDING);
    rental.setStartDate(dto.startDate());
    rental.setEndDate(dto.endDate());
    rental.setDailyRate(BigDecimal.valueOf(dto.dailyRate()));
    rental.setCurrency(dto.currency());
    rental.setDepositAmount(
        dto.depositAmount() != null ? BigDecimal.valueOf(dto.depositAmount()) : null);
    rental.setDepositReturned(false);
    rental.setNotes(dto.notes());
    rental.setPickupLocation(dto.pickupLocation());
    rental.setReturnLocation(dto.returnLocation());

    // Calculate total amount
    long days = ChronoUnit.DAYS.between(dto.startDate(), dto.endDate()) + 1;
    rental.setTotalAmount(rental.getDailyRate().multiply(BigDecimal.valueOf(days)));

    return rental;
  }
}

