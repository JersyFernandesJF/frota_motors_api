package com.example.kubico.domain.service;

import com.example.kubico.domain.enums.VehicleStatus;
import com.example.kubico.domain.enums.VehicleType;
import com.example.kubico.domain.model.Agency;
import com.example.kubico.domain.model.User;
import com.example.kubico.domain.model.Vehicle;
import com.example.kubico.infrastructure.dto.VehicleCreateDTO;
import com.example.kubico.infrastructure.mapper.VehicleMapper;
import com.example.kubico.infrastructure.persistence.AgencyRepository;
import com.example.kubico.infrastructure.persistence.UserRepository;
import com.example.kubico.infrastructure.persistence.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

  @Autowired private VehicleRepository vehicleRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private AgencyRepository agencyRepository;

  public Vehicle create(VehicleCreateDTO dto) {
    User owner =
        userRepository
            .findById(dto.ownerId())
            .orElseThrow(() -> new EntityNotFoundException("Owner not found"));

    Agency agency = null;
    if (dto.agencyId() != null) {
      agency =
          agencyRepository
              .findById(dto.agencyId())
              .orElseThrow(() -> new EntityNotFoundException("Agency not found"));
    }

    Vehicle vehicle = VehicleMapper.toEntity(dto, owner, agency);
    return vehicleRepository.save(vehicle);
  }

  public List<Vehicle> getAll() {
    return vehicleRepository.findAll();
  }

  public Page<Vehicle> getAll(Pageable pageable) {
    return vehicleRepository.findAll(pageable);
  }

  public Vehicle getById(UUID id) {
    return vehicleRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));
  }

  public Vehicle update(UUID id, VehicleCreateDTO dto) {
    Vehicle existing =
        vehicleRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));

    User owner =
        userRepository
            .findById(dto.ownerId())
            .orElseThrow(() -> new EntityNotFoundException("Owner not found"));

    Agency agency = null;
    if (dto.agencyId() != null) {
      agency =
          agencyRepository
              .findById(dto.agencyId())
              .orElseThrow(() -> new EntityNotFoundException("Agency not found"));
    }

    existing.setOwner(owner);
    existing.setAgency(agency);
    existing.setType(dto.type());
    existing.setStatus(dto.status());
    existing.setBrand(dto.brand());
    existing.setModel(dto.model());
    existing.setYear(dto.year());
    existing.setColor(dto.color());
    existing.setLicensePlate(dto.licensePlate());
    existing.setVin(dto.vin());
    existing.setMileageKm(dto.mileageKm());
    existing.setPrice(BigDecimal.valueOf(dto.price()));
    existing.setCurrency(dto.currency());
    existing.setDescription(dto.description());
    existing.setFuelType(dto.fuelType());
    existing.setTransmissionType(dto.transmissionType());
    existing.setEngineSize(dto.engineSize());
    existing.setHorsePower(dto.horsePower());
    existing.setNumberOfDoors(dto.numberOfDoors());
    existing.setNumberOfSeats(dto.numberOfSeats());
    existing.setPreviousOwners(dto.previousOwners());
    existing.setAccidentHistory(dto.accidentHistory());

    return vehicleRepository.save(existing);
  }

  public void delete(UUID id) {
    if (!vehicleRepository.existsById(id)) {
      throw new EntityNotFoundException("Vehicle not found");
    }
    vehicleRepository.deleteById(id);
  }

  public List<Vehicle> search(
      VehicleType type,
      VehicleStatus status,
      BigDecimal minPrice,
      BigDecimal maxPrice,
      String brand,
      String model,
      Integer minYear,
      Integer maxYear,
      String fuelType) {
    return vehicleRepository.search(
        type, status, minPrice, maxPrice, brand, model, minYear, maxYear, fuelType);
  }

  public Page<Vehicle> search(
      VehicleType type,
      VehicleStatus status,
      BigDecimal minPrice,
      BigDecimal maxPrice,
      String brand,
      String model,
      Integer minYear,
      Integer maxYear,
      String fuelType,
      Pageable pageable) {
    // Validate price range
    if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
      throw new IllegalArgumentException("minPrice cannot be greater than maxPrice");
    }
    // Validate year range
    if (minYear != null && maxYear != null && minYear > maxYear) {
      throw new IllegalArgumentException("minYear cannot be greater than maxYear");
    }
    return vehicleRepository.searchPageable(
        type, status, minPrice, maxPrice, brand, model, minYear, maxYear, fuelType, pageable);
  }

  public List<Vehicle> getByOwner(UUID ownerId) {
    return vehicleRepository.findByOwnerId(ownerId);
  }

  public Page<Vehicle> getByOwner(UUID ownerId, Pageable pageable) {
    return vehicleRepository.findByOwnerId(ownerId, pageable);
  }

  public List<Vehicle> getByAgency(UUID agencyId) {
    return vehicleRepository.findByAgencyId(agencyId);
  }

  public Page<Vehicle> getByAgency(UUID agencyId, Pageable pageable) {
    return vehicleRepository.findByAgencyId(agencyId, pageable);
  }

  public List<Vehicle> getByType(VehicleType type) {
    return vehicleRepository.findByType(type);
  }

  public Page<Vehicle> getByType(VehicleType type, Pageable pageable) {
    return vehicleRepository.findByType(type, pageable);
  }

  public List<Vehicle> getByStatus(VehicleStatus status) {
    return vehicleRepository.findByStatus(status);
  }

  public Page<Vehicle> getByStatus(VehicleStatus status, Pageable pageable) {
    return vehicleRepository.findByStatus(status, pageable);
  }
}

