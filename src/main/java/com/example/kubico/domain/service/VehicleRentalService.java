package com.example.kubico.domain.service;

import com.example.kubico.domain.enums.RentalStatus;
import com.example.kubico.domain.model.Agency;
import com.example.kubico.domain.model.User;
import com.example.kubico.domain.model.Vehicle;
import com.example.kubico.domain.model.VehicleRental;
import com.example.kubico.infrastructure.dto.VehicleRentalCreateDTO;
import com.example.kubico.infrastructure.mapper.VehicleRentalMapper;
import com.example.kubico.infrastructure.persistence.AgencyRepository;
import com.example.kubico.infrastructure.persistence.UserRepository;
import com.example.kubico.infrastructure.persistence.VehicleRentalRepository;
import com.example.kubico.infrastructure.persistence.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VehicleRentalService {

  @Autowired private VehicleRentalRepository vehicleRentalRepository;

  @Autowired private VehicleRepository vehicleRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private AgencyRepository agencyRepository;

  public VehicleRental create(VehicleRentalCreateDTO dto) {
    Vehicle vehicle =
        vehicleRepository
            .findById(dto.vehicleId())
            .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));

    User renter =
        userRepository
            .findById(dto.renterId())
            .orElseThrow(() -> new EntityNotFoundException("Renter not found"));

    Agency agency = null;
    if (dto.agencyId() != null) {
      agency =
          agencyRepository
              .findById(dto.agencyId())
              .orElseThrow(() -> new EntityNotFoundException("Agency not found"));
    }

    // Check for conflicting rentals
    List<VehicleRental> conflicts =
        vehicleRentalRepository.findConflictingRentals(
            dto.vehicleId(), dto.startDate(), dto.endDate());
    if (!conflicts.isEmpty()) {
      throw new IllegalStateException("Vehicle is already rented for the selected dates");
    }

    VehicleRental rental = VehicleRentalMapper.toEntity(dto, vehicle, renter, agency);
    return vehicleRentalRepository.save(rental);
  }

  public List<VehicleRental> getAll() {
    return vehicleRentalRepository.findAll();
  }

  public Page<VehicleRental> getAll(Pageable pageable) {
    return vehicleRentalRepository.findAll(pageable);
  }

  public VehicleRental getById(UUID id) {
    return vehicleRentalRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Vehicle rental not found"));
  }

  public VehicleRental update(UUID id, VehicleRentalCreateDTO dto) {
    VehicleRental existing =
        vehicleRentalRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Vehicle rental not found"));

    Vehicle vehicle =
        vehicleRepository
            .findById(dto.vehicleId())
            .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));

    User renter =
        userRepository
            .findById(dto.renterId())
            .orElseThrow(() -> new EntityNotFoundException("Renter not found"));

    Agency agency = null;
    if (dto.agencyId() != null) {
      agency =
          agencyRepository
              .findById(dto.agencyId())
              .orElseThrow(() -> new EntityNotFoundException("Agency not found"));
    }

    // Check for conflicting rentals (excluding current rental)
    List<VehicleRental> conflicts =
        vehicleRentalRepository.findConflictingRentals(
            dto.vehicleId(), dto.startDate(), dto.endDate());
    conflicts.removeIf(c -> c.getId().equals(id));
    if (!conflicts.isEmpty()) {
      throw new IllegalStateException("Vehicle is already rented for the selected dates");
    }

    existing.setVehicle(vehicle);
    existing.setRenter(renter);
    existing.setAgency(agency);
    existing.setStartDate(dto.startDate());
    existing.setEndDate(dto.endDate());
    existing.setDailyRate(
        java.math.BigDecimal.valueOf(dto.dailyRate()));
    existing.setCurrency(dto.currency());
    existing.setDepositAmount(
        dto.depositAmount() != null
            ? java.math.BigDecimal.valueOf(dto.depositAmount())
            : null);
    existing.setNotes(dto.notes());
    existing.setPickupLocation(dto.pickupLocation());
    existing.setReturnLocation(dto.returnLocation());

    // Recalculate total amount
    long days =
        java.time.temporal.ChronoUnit.DAYS.between(dto.startDate(), dto.endDate()) + 1;
    existing.setTotalAmount(existing.getDailyRate().multiply(java.math.BigDecimal.valueOf(days)));

    return vehicleRentalRepository.save(existing);
  }

  public void delete(UUID id) {
    if (!vehicleRentalRepository.existsById(id)) {
      throw new EntityNotFoundException("Vehicle rental not found");
    }
    vehicleRentalRepository.deleteById(id);
  }

  public VehicleRental confirmRental(UUID id) {
    VehicleRental rental = getById(id);
    if (rental.getStatus() != RentalStatus.PENDING) {
      throw new IllegalStateException("Only pending rentals can be confirmed");
    }
    rental.setStatus(RentalStatus.ACTIVE);
    return vehicleRentalRepository.save(rental);
  }

  public VehicleRental completeRental(UUID id, Integer mileageAtReturn) {
    VehicleRental rental = getById(id);
    if (rental.getStatus() != RentalStatus.ACTIVE) {
      throw new IllegalStateException("Only active rentals can be completed");
    }
    rental.setStatus(RentalStatus.COMPLETED);
    rental.setActualReturnDate(LocalDate.now());
    rental.setMileageAtReturn(mileageAtReturn);
    return vehicleRentalRepository.save(rental);
  }

  public VehicleRental cancelRental(UUID id) {
    VehicleRental rental = getById(id);
    if (rental.getStatus() == RentalStatus.COMPLETED) {
      throw new IllegalStateException("Completed rentals cannot be cancelled");
    }
    rental.setStatus(RentalStatus.CANCELLED);
    return vehicleRentalRepository.save(rental);
  }

  public List<VehicleRental> search(
      UUID renterId, UUID vehicleId, RentalStatus status, LocalDate startDate, LocalDate endDate) {
    return vehicleRentalRepository.search(renterId, vehicleId, status, startDate, endDate);
  }

  public Page<VehicleRental> search(
      UUID renterId,
      UUID vehicleId,
      RentalStatus status,
      LocalDate startDate,
      LocalDate endDate,
      Pageable pageable) {
    // Validate date range
    if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("startDate cannot be after endDate");
    }
    return vehicleRentalRepository.searchPageable(
        renterId, vehicleId, status, startDate, endDate, pageable);
  }

  public List<VehicleRental> getByRenter(UUID renterId) {
    return vehicleRentalRepository.findByRenterId(renterId);
  }

  public Page<VehicleRental> getByRenter(UUID renterId, Pageable pageable) {
    return vehicleRentalRepository.findByRenterId(renterId, pageable);
  }

  public List<VehicleRental> getByVehicle(UUID vehicleId) {
    return vehicleRentalRepository.findByVehicleId(vehicleId);
  }

  public Page<VehicleRental> getByVehicle(UUID vehicleId, Pageable pageable) {
    return vehicleRentalRepository.findByVehicleId(vehicleId, pageable);
  }

  public List<VehicleRental> getByAgency(UUID agencyId) {
    return vehicleRentalRepository.findByAgencyId(agencyId);
  }

  public Page<VehicleRental> getByAgency(UUID agencyId, Pageable pageable) {
    return vehicleRentalRepository.findByAgencyId(agencyId, pageable);
  }

  public List<VehicleRental> getByStatus(RentalStatus status) {
    return vehicleRentalRepository.findByStatus(status);
  }

  public Page<VehicleRental> getByStatus(RentalStatus status, Pageable pageable) {
    return vehicleRentalRepository.findByStatus(status, pageable);
  }
}

