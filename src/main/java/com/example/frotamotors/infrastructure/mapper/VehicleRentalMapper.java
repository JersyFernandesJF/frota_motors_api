package com.example.frotamotors.infrastructure.mapper;

import com.example.frotamotors.domain.enums.RentalStatus;
import com.example.frotamotors.domain.model.Agency;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.domain.model.VehicleRental;
import com.example.frotamotors.infrastructure.dto.VehicleRentalCreateDTO;
import com.example.frotamotors.infrastructure.dto.VehicleRentalResponseDTO;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

public class VehicleRentalMapper {

  private VehicleRentalMapper() {}

  public static VehicleRentalResponseDTO toResponse(VehicleRental rental) {
    return new VehicleRentalResponseDTO(
        rental.getId(),
        rental.getVehicle() != null ? VehicleMapper.toResponse(rental.getVehicle()) : null,
        rental.getRenter() != null ? UserMapper.toResponse(rental.getRenter()) : null,
        rental.getAgency() != null ? AgencyMapper.toResponse(rental.getAgency()) : null,
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
    rental.setCurrency(dto.currency().name());
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
