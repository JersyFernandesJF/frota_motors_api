package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.enums.ListingModerationStatus;
import com.example.frotamotors.domain.enums.VehicleStatus;
import com.example.frotamotors.domain.enums.VehicleType;
import com.example.frotamotors.domain.model.Agency;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.domain.model.VehicleHistory;
import com.example.frotamotors.infrastructure.dto.ExportRequestDTO;
import com.example.frotamotors.infrastructure.dto.VehicleCreateDTO;
import com.example.frotamotors.infrastructure.mapper.VehicleMapper;
import com.example.frotamotors.infrastructure.persistence.AgencyRepository;
import com.example.frotamotors.infrastructure.persistence.UserRepository;
import com.example.frotamotors.infrastructure.persistence.VehicleHistoryRepository;
import com.example.frotamotors.infrastructure.persistence.VehicleRepository;
import com.example.frotamotors.infrastructure.util.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehicleService {

  @Autowired private VehicleRepository vehicleRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private AgencyRepository agencyRepository;

  @Autowired private VehicleHistoryRepository vehicleHistoryRepository;

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

  @Transactional(readOnly = true)
  public Page<Vehicle> getAll(Pageable pageable) {
    Page<Vehicle> page = vehicleRepository.findAll(pageable);
    // Force initialization of lazy relationships to avoid LazyInitializationException
    page.getContent().forEach(vehicle -> {
      if (vehicle.getOwner() != null) {
        vehicle.getOwner().getId(); // Trigger lazy load
      }
      if (vehicle.getAgency() != null) {
        vehicle.getAgency().getId(); // Trigger lazy load
      }
      if (vehicle.getMedia() != null) {
        vehicle.getMedia().size(); // Trigger lazy load for media collection
      }
    });
    return page;
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

  @Transactional(readOnly = true)
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
    Page<Vehicle> page = vehicleRepository.searchPageable(
        type, status, minPrice, maxPrice, brand, model, minYear, maxYear, fuelType, pageable);
    // Force initialization of lazy relationships to avoid LazyInitializationException
    page.getContent().forEach(vehicle -> {
      if (vehicle.getOwner() != null) {
        vehicle.getOwner().getId(); // Trigger lazy load
      }
      if (vehicle.getAgency() != null) {
        vehicle.getAgency().getId(); // Trigger lazy load
      }
      if (vehicle.getMedia() != null) {
        vehicle.getMedia().size(); // Trigger lazy load for media collection
      }
    });
    return page;
  }

  public List<Vehicle> getByOwner(UUID ownerId) {
    return vehicleRepository.findByOwnerId(ownerId);
  }

  @Transactional(readOnly = true)
  public Page<Vehicle> getByOwner(UUID ownerId, Pageable pageable) {
    Page<Vehicle> page = vehicleRepository.findByOwnerId(ownerId, pageable);
    // Force initialization of lazy relationships to avoid LazyInitializationException
    page.getContent().forEach(vehicle -> {
      if (vehicle.getOwner() != null) {
        vehicle.getOwner().getId(); // Trigger lazy load
      }
      if (vehicle.getAgency() != null) {
        vehicle.getAgency().getId(); // Trigger lazy load
      }
      if (vehicle.getMedia() != null) {
        vehicle.getMedia().size(); // Trigger lazy load for media collection
      }
    });
    return page;
  }

  public List<Vehicle> getByAgency(UUID agencyId) {
    return vehicleRepository.findByAgencyId(agencyId);
  }

  @Transactional(readOnly = true)
  public Page<Vehicle> getByAgency(UUID agencyId, Pageable pageable) {
    Page<Vehicle> page = vehicleRepository.findByAgencyId(agencyId, pageable);
    // Force initialization of lazy relationships to avoid LazyInitializationException
    page.getContent().forEach(vehicle -> {
      if (vehicle.getOwner() != null) {
        vehicle.getOwner().getId(); // Trigger lazy load
      }
      if (vehicle.getAgency() != null) {
        vehicle.getAgency().getId(); // Trigger lazy load
      }
      if (vehicle.getMedia() != null) {
        vehicle.getMedia().size(); // Trigger lazy load for media collection
      }
    });
    return page;
  }

  public List<Vehicle> getByType(VehicleType type) {
    return vehicleRepository.findByType(type);
  }

  @Transactional(readOnly = true)
  public Page<Vehicle> getByType(VehicleType type, Pageable pageable) {
    Page<Vehicle> page = vehicleRepository.findByType(type, pageable);
    // Force initialization of lazy relationships to avoid LazyInitializationException
    page.getContent().forEach(vehicle -> {
      if (vehicle.getOwner() != null) {
        vehicle.getOwner().getId(); // Trigger lazy load
      }
      if (vehicle.getAgency() != null) {
        vehicle.getAgency().getId(); // Trigger lazy load
      }
      if (vehicle.getMedia() != null) {
        vehicle.getMedia().size(); // Trigger lazy load for media collection
      }
    });
    return page;
  }

  public List<Vehicle> getByStatus(VehicleStatus status) {
    return vehicleRepository.findByStatus(status);
  }

  @Transactional(readOnly = true)
  public Page<Vehicle> getByStatus(VehicleStatus status, Pageable pageable) {
    Page<Vehicle> page = vehicleRepository.findByStatus(status, pageable);
    // Force initialization of lazy relationships to avoid LazyInitializationException
    page.getContent().forEach(vehicle -> {
      if (vehicle.getOwner() != null) {
        vehicle.getOwner().getId(); // Trigger lazy load
      }
      if (vehicle.getAgency() != null) {
        vehicle.getAgency().getId(); // Trigger lazy load
      }
      if (vehicle.getMedia() != null) {
        vehicle.getMedia().size(); // Trigger lazy load for media collection
      }
    });
    return page;
  }

  @Transactional
  public Vehicle approveVehicle(UUID id, String notes) {
    Vehicle vehicle = getById(id);
    UUID adminId = SecurityUtils.getCurrentUserId();
    User admin = userRepository.findById(adminId).orElse(null);

    vehicle.setModerationStatus(ListingModerationStatus.APPROVED);
    vehicle.setApprovedBy(admin);
    vehicle.setApprovedAt(LocalDateTime.now());
    vehicle.setPublishedAt(LocalDateTime.now());
    vehicle.setRejectedBy(null);
    vehicle.setRejectedAt(null);
    vehicle.setRejectionReason(null);

    Vehicle saved = vehicleRepository.save(vehicle);

    // Log history
    logVehicleHistory(
        saved.getId(),
        "approved",
        "pending",
        "approved",
        adminId,
        notes != null ? notes : "Aprovado sem restrições");

    return saved;
  }

  @Transactional
  public Vehicle rejectVehicle(UUID id, String reason, Boolean notifySeller) {
    Vehicle vehicle = getById(id);
    UUID adminId = SecurityUtils.getCurrentUserId();
    User admin = userRepository.findById(adminId).orElse(null);

    vehicle.setModerationStatus(ListingModerationStatus.REJECTED);
    vehicle.setRejectedBy(admin);
    vehicle.setRejectedAt(LocalDateTime.now());
    vehicle.setRejectionReason(reason);
    vehicle.setApprovedBy(null);
    vehicle.setApprovedAt(null);
    vehicle.setPublishedAt(null);

    Vehicle saved = vehicleRepository.save(vehicle);

    // Log history
    logVehicleHistory(saved.getId(), "rejected", "pending", "rejected", adminId, reason);

    // TODO: Notify seller if requested
    if (notifySeller != null && notifySeller) {
      // Implement notification
    }

    return saved;
  }

  public Page<VehicleHistory> getVehicleHistory(UUID vehicleId, Pageable pageable) {
    return vehicleHistoryRepository.findByVehicleIdOrderByChangedAtDesc(vehicleId, pageable);
  }

  public String exportVehicles(ExportRequestDTO request) {
    // TODO: Implement export via ExportService
    return "Export functionality will be implemented with ExportService";
  }

  private void logVehicleHistory(
      UUID vehicleId,
      String action,
      String oldValue,
      String newValue,
      UUID changedById,
      String notes) {
    VehicleHistory history = new VehicleHistory();
    Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null);
    if (vehicle != null) {
      history.setVehicle(vehicle);
      history.setAction(action);
      history.setOldValue(oldValue);
      history.setNewValue(newValue);
      if (changedById != null) {
        User changedBy = userRepository.findById(changedById).orElse(null);
        history.setChangedBy(changedBy);
      }
      vehicleHistoryRepository.save(history);
    }
  }
}
