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
import com.example.frotamotors.infrastructure.util.RsqlSpecificationBuilder;
import com.example.frotamotors.infrastructure.util.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class VehicleService {

  @Autowired private VehicleRepository vehicleRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private AgencyRepository agencyRepository;

  @Autowired private VehicleHistoryRepository vehicleHistoryRepository;

  @Autowired private RsqlSpecificationBuilder rsqlSpecificationBuilder;

  @Autowired private SubscriptionService subscriptionService;

  @Transactional
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

      // Validate subscription and vehicle limits for agencies
      if (!subscriptionService.canCreateVehicle(agency.getId())) {
        throw new IllegalStateException(
            "Agency has reached the maximum number of vehicles allowed by subscription");
      }
    }

    Vehicle vehicle = VehicleMapper.toEntity(dto, owner, agency);
    Vehicle saved = vehicleRepository.save(vehicle);

    // Increment vehicle count for agency if applicable
    if (agency != null) {
      subscriptionService.incrementVehicleCount(agency.getId());
    }

    return saved;
  }

  public List<Vehicle> getAll() {
    return vehicleRepository.findAll();
  }

  @Transactional(readOnly = true)
  public Page<Vehicle> getAll(Pageable pageable) {
    Page<Vehicle> page = vehicleRepository.findAllWithMedia(pageable);
    // Force initialization of lazy relationships to avoid LazyInitializationException
    page.getContent()
        .forEach(
            vehicle -> {
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

  @Transactional(readOnly = true)
  public Vehicle getById(UUID id) {
    Vehicle vehicle =
        vehicleRepository
            .findByIdWithMedia(id)
            .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));

    // Force initialization of lazy relationships to avoid LazyInitializationException
    if (vehicle.getOwner() != null) {
      vehicle.getOwner().getId(); // Trigger lazy load
    }
    if (vehicle.getAgency() != null) {
      vehicle.getAgency().getId(); // Trigger lazy load
    }
    if (vehicle.getMedia() != null) {
      vehicle.getMedia().size(); // Trigger lazy load for media collection
    }

    return vehicle;
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
    existing.setCurrency(dto.currency().name());
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

  @Transactional
  public void delete(UUID id) {
    Vehicle vehicle =
        vehicleRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));

    Agency agency = vehicle.getAgency();

    vehicleRepository.deleteById(id);

    // Decrement vehicle count for agency if applicable
    if (agency != null) {
      subscriptionService.decrementVehicleCount(agency.getId());
    }
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
        type != null ? type.name() : null,
        status != null ? status.name() : null,
        minPrice,
        maxPrice,
        brand,
        model,
        minYear,
        maxYear,
        fuelType);
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
      String transmission,
      Integer minMileage,
      Integer maxMileage,
      String location,
      String search,
      String sort,
      LocalDateTime startDate,
      LocalDateTime endDate,
      Pageable pageable) {
    // Validate price range
    if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
      throw new IllegalArgumentException("minPrice cannot be greater than maxPrice");
    }
    // Validate year range
    if (minYear != null && maxYear != null && minYear > maxYear) {
      throw new IllegalArgumentException("minYear cannot be greater than maxYear");
    }
    // Validate mileage range
    if (minMileage != null && maxMileage != null && minMileage > maxMileage) {
      throw new IllegalArgumentException("minMileage cannot be greater than maxMileage");
    }

    // Apply dynamic sorting based on 'sort' parameter
    // Note: For native queries, we need to use SQL column names, not Java property names
    Pageable effectivePageable = pageable;
    Sort sortSpec;
    if (sort != null && !sort.isBlank()) {
      switch (sort) {
        case "price-asc":
          sortSpec = Sort.by(Sort.Direction.ASC, "price");
          break;
        case "price-desc":
          sortSpec = Sort.by(Sort.Direction.DESC, "price");
          break;
        case "year-desc":
          sortSpec = Sort.by(Sort.Direction.DESC, "year");
          break;
        case "mileage-asc":
          sortSpec = Sort.by(Sort.Direction.ASC, "mileage_km");
          break;
        case "recent":
        default:
          sortSpec = Sort.by(Sort.Direction.DESC, "created_at");
          break;
      }
    } else {
      // Default sort: most recent first (using SQL column name for native query)
      sortSpec = Sort.by(Sort.Direction.DESC, "created_at");
    }
    effectivePageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortSpec);

    Page<Vehicle> page =
        vehicleRepository.searchPageable(
            type != null ? type.name() : null,
            status != null ? status.name() : null,
            minPrice,
            maxPrice,
            brand,
            model,
            minYear,
            maxYear,
            fuelType,
            transmission,
            minMileage,
            maxMileage,
            startDate,
            endDate,
            search,
            effectivePageable);
    // Force initialization of lazy relationships to avoid LazyInitializationException
    page.getContent()
        .forEach(
            vehicle -> {
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

  /**
   * Search vehicles using RSQL filter string.
   *
   * <p>RSQL allows dynamic querying with a simple syntax. Examples:
   *
   * <ul>
   *   <li>type==CAR;price>=10000;price<=50000
   *   <li>brand==Toyota;status==FOR_SALE
   *   <li>year>=2020;fuelType==GASOLINE;transmissionType==AUTOMATIC
   *   <li>owner.email==user@example.com
   * </ul>
   *
   * @param rsqlFilter RSQL filter string (e.g., "type==CAR;price>=10000")
   * @param pageable pagination and sorting parameters
   * @return page of vehicles matching the RSQL filter
   */
  @Transactional(readOnly = true)
  public Page<Vehicle> search(String rsqlFilter, Pageable pageable) {
    Specification<Vehicle> spec = rsqlSpecificationBuilder.build(rsqlFilter, Vehicle.class);

    // Processar sort do Pageable para converter valores como 'recent' para 'createdAt,desc'
    Pageable effectivePageable = pageable;
    Sort sort = pageable.getSort();
    if (sort.isSorted()) {
      Sort.Order firstOrder = sort.iterator().next();
      String property = firstOrder.getProperty();
      Sort.Direction direction = firstOrder.getDirection();
      
      // Mapear valores de sort conhecidos
      String mappedProperty = property;
      Sort.Direction mappedDirection = direction;
      
      if ("recent".equals(property)) {
        mappedProperty = "createdAt";
        mappedDirection = Sort.Direction.DESC; // recent sempre é DESC
      } else if ("price_asc".equals(property) || "price-asc".equals(property)) {
        mappedProperty = "price";
        mappedDirection = Sort.Direction.ASC;
      } else if ("price_desc".equals(property) || "price-desc".equals(property)) {
        mappedProperty = "price";
        mappedDirection = Sort.Direction.DESC;
      } else if ("mileage_asc".equals(property) || "mileage-asc".equals(property)) {
        mappedProperty = "mileageKm";
        mappedDirection = Sort.Direction.ASC;
      } else if ("year_desc".equals(property) || "year-desc".equals(property)) {
        mappedProperty = "year";
        mappedDirection = Sort.Direction.DESC;
      }
      
      // Se a propriedade foi mapeada ou a direção mudou, criar novo Sort
      if (!mappedProperty.equals(property) || mappedDirection != direction) {
        Sort newSort = Sort.by(mappedDirection, mappedProperty);
        effectivePageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), newSort);
      }
    } else {
      // Se não há sort, usar padrão: createdAt DESC
      Sort defaultSort = Sort.by(Sort.Direction.DESC, "createdAt");
      effectivePageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);
    }

    Page<Vehicle> page = vehicleRepository.findAll(spec, effectivePageable);

    // Force initialization of lazy relationships to avoid LazyInitializationException
    page.getContent()
        .forEach(
            vehicle -> {
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

  @Transactional(readOnly = true)
  public List<Object[]> getBrandSuggestions(String search) {
    return vehicleRepository.findBrandsWithCount(search);
  }

  @Transactional(readOnly = true)
  public List<Object[]> getModelSuggestions(String brand, String search) {
    return vehicleRepository.findModelsWithCount(brand, search);
  }

  public List<Vehicle> getByOwner(UUID ownerId) {
    return vehicleRepository.findByOwnerId(ownerId);
  }

  @Transactional(readOnly = true)
  public Page<Vehicle> getByOwner(UUID ownerId, Pageable pageable) {
    Page<Vehicle> page = vehicleRepository.findByOwnerId(ownerId, pageable);
    // Force initialization of lazy relationships to avoid LazyInitializationException
    page.getContent()
        .forEach(
            vehicle -> {
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
    page.getContent()
        .forEach(
            vehicle -> {
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
    page.getContent()
        .forEach(
            vehicle -> {
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
    page.getContent()
        .forEach(
            vehicle -> {
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

  @Transactional
  public List<Vehicle> bulkApprove(List<UUID> vehicleIds, String notes) {
    UUID adminId = SecurityUtils.getCurrentUserId();
    User admin = userRepository.findById(adminId).orElse(null);
    List<Vehicle> approvedVehicles = new java.util.ArrayList<>();

    for (UUID vehicleId : vehicleIds) {
      try {
        Vehicle vehicle = getById(vehicleId);
        vehicle.setModerationStatus(ListingModerationStatus.APPROVED);
        vehicle.setApprovedBy(admin);
        vehicle.setApprovedAt(LocalDateTime.now());
        vehicle.setPublishedAt(LocalDateTime.now());
        vehicle.setRejectedBy(null);
        vehicle.setRejectedAt(null);
        vehicle.setRejectionReason(null);

        Vehicle saved = vehicleRepository.save(vehicle);
        approvedVehicles.add(saved);

        // Log history
        logVehicleHistory(
            saved.getId(),
            "approved",
            "pending",
            "approved",
            adminId,
            notes != null ? notes : "Aprovado em lote");
      } catch (Exception e) {
        // Log error but continue with other vehicles
        log.error("Error approving vehicle {}: {}", vehicleId, e.getMessage());
      }
    }

    return approvedVehicles;
  }

  @Transactional
  public List<Vehicle> bulkReject(List<UUID> vehicleIds, String reason) {
    UUID adminId = SecurityUtils.getCurrentUserId();
    User admin = userRepository.findById(adminId).orElse(null);
    List<Vehicle> rejectedVehicles = new java.util.ArrayList<>();

    for (UUID vehicleId : vehicleIds) {
      try {
        Vehicle vehicle = getById(vehicleId);
        vehicle.setModerationStatus(ListingModerationStatus.REJECTED);
        vehicle.setRejectedBy(admin);
        vehicle.setRejectedAt(LocalDateTime.now());
        vehicle.setRejectionReason(reason);
        vehicle.setApprovedBy(null);
        vehicle.setApprovedAt(null);
        vehicle.setPublishedAt(null);

        Vehicle saved = vehicleRepository.save(vehicle);
        rejectedVehicles.add(saved);

        // Log history
        logVehicleHistory(saved.getId(), "rejected", "pending", "rejected", adminId, reason);
      } catch (Exception e) {
        // Log error but continue with other vehicles
        log.error("Error rejecting vehicle {}: {}", vehicleId, e.getMessage());
      }
    }

    return rejectedVehicles;
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
