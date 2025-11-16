package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.enums.OrderStatus;
import com.example.frotamotors.domain.enums.OrderType;
import com.example.frotamotors.domain.model.Part;
import com.example.frotamotors.domain.model.Purchase;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.infrastructure.dto.PurchaseCreateDTO;
import com.example.frotamotors.infrastructure.mapper.PurchaseMapper;
import com.example.frotamotors.infrastructure.persistence.PartRepository;
import com.example.frotamotors.infrastructure.persistence.PurchaseRepository;
import com.example.frotamotors.infrastructure.persistence.UserRepository;
import com.example.frotamotors.infrastructure.persistence.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PurchaseService {

  @Autowired private PurchaseRepository purchaseRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private VehicleRepository vehicleRepository;

  @Autowired private PartRepository partRepository;

  @Transactional
  public Purchase create(PurchaseCreateDTO dto) {
    User buyer =
        userRepository
            .findById(dto.buyerId())
            .orElseThrow(() -> new EntityNotFoundException("Buyer not found"));

    Vehicle vehicle = null;
    Part part = null;

    // Validate that exactly one item is provided based on type
    if (dto.type() == OrderType.VEHICLE) {
      if (dto.vehicleId() == null) {
        throw new IllegalArgumentException("Vehicle ID is required for vehicle orders");
      }
      vehicle =
          vehicleRepository
              .findById(dto.vehicleId())
              .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));
      if (vehicle.getStatus().name().equals("SOLD")) {
        throw new IllegalStateException("Vehicle is already sold");
      }
    } else if (dto.type() == OrderType.PART) {
      if (dto.partId() == null) {
        throw new IllegalArgumentException("Part ID is required for part orders");
      }
      part =
          partRepository
              .findById(dto.partId())
              .orElseThrow(() -> new EntityNotFoundException("Part not found"));
      if (part.getStatus().name().equals("SOLD")) {
        throw new IllegalStateException("Part is already sold");
      }
      // Check quantity availability
      if (part.getQuantityAvailable() != null && part.getQuantityAvailable() < dto.quantity()) {
        throw new IllegalStateException("Insufficient quantity available");
      }
    }

    Purchase purchase = PurchaseMapper.toEntity(dto, buyer, vehicle, part);
    Purchase saved = purchaseRepository.save(purchase);

    // Update item status and quantity
    if (vehicle != null) {
      // Vehicle status will be updated when order is confirmed
    } else if (part != null) {
      int newQuantity =
          part.getQuantityAvailable() != null ? part.getQuantityAvailable() - dto.quantity() : 0;
      part.setQuantityAvailable(newQuantity);
      if (newQuantity == 0) {
        part.setStatus(com.example.frotamotors.domain.enums.PartStatus.OUT_OF_STOCK);
      }
      partRepository.save(part);
    }

    return saved;
  }

  public List<Purchase> getAll() {
    return purchaseRepository.findAll();
  }

  public Page<Purchase> getAll(Pageable pageable) {
    return purchaseRepository.findAll(pageable);
  }

  public Purchase getById(UUID id) {
    return purchaseRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Purchase not found"));
  }

  @Transactional
  public Purchase updateStatus(UUID id, OrderStatus status, String trackingNumber) {
    Purchase purchase = getById(id);
    purchase.setStatus(status);
    if (trackingNumber != null) {
      purchase.setTrackingNumber(trackingNumber);
    }
    return purchaseRepository.save(purchase);
  }

  @Transactional
  public Purchase confirmOrder(UUID id) {
    Purchase purchase = getById(id);
    if (purchase.getStatus() != OrderStatus.PENDING) {
      throw new IllegalStateException("Only pending orders can be confirmed");
    }
    purchase.setStatus(OrderStatus.CONFIRMED);

    // Update item status
    if (purchase.getVehicle() != null) {
      purchase.getVehicle().setStatus(com.example.frotamotors.domain.enums.VehicleStatus.SOLD);
      vehicleRepository.save(purchase.getVehicle());
    } else if (purchase.getPart() != null) {
      // Part status already updated on creation
    }

    return purchaseRepository.save(purchase);
  }

  @Transactional
  public Purchase cancelOrder(UUID id) {
    Purchase purchase = getById(id);
    if (purchase.getStatus() == OrderStatus.DELIVERED
        || purchase.getStatus() == OrderStatus.SHIPPED) {
      throw new IllegalStateException("Cannot cancel shipped or delivered orders");
    }
    purchase.setStatus(OrderStatus.CANCELLED);

    // Restore item availability
    if (purchase.getVehicle() != null) {
      purchase.getVehicle().setStatus(com.example.frotamotors.domain.enums.VehicleStatus.FOR_SALE);
      vehicleRepository.save(purchase.getVehicle());
    } else if (purchase.getPart() != null) {
      int restoredQuantity =
          purchase.getPart().getQuantityAvailable() != null
              ? purchase.getPart().getQuantityAvailable() + purchase.getQuantity()
              : purchase.getQuantity();
      purchase.getPart().setQuantityAvailable(restoredQuantity);
      purchase.getPart().setStatus(com.example.frotamotors.domain.enums.PartStatus.AVAILABLE);
      partRepository.save(purchase.getPart());
    }

    return purchaseRepository.save(purchase);
  }

  public void delete(UUID id) {
    if (!purchaseRepository.existsById(id)) {
      throw new EntityNotFoundException("Purchase not found");
    }
    purchaseRepository.deleteById(id);
  }

  public List<Purchase> search(UUID buyerId, OrderStatus status, OrderType type) {
    return purchaseRepository.search(buyerId, status, type);
  }

  public Page<Purchase> search(
      UUID buyerId, OrderStatus status, OrderType type, Pageable pageable) {
    return purchaseRepository.searchPageable(buyerId, status, type, pageable);
  }

  public List<Purchase> getByBuyer(UUID buyerId) {
    return purchaseRepository.findByBuyerId(buyerId);
  }

  public Page<Purchase> getByBuyer(UUID buyerId, Pageable pageable) {
    return purchaseRepository.findByBuyerId(buyerId, pageable);
  }

  public List<Purchase> getByStatus(OrderStatus status) {
    return purchaseRepository.findByStatus(status);
  }

  public Page<Purchase> getByStatus(OrderStatus status, Pageable pageable) {
    return purchaseRepository.findByStatus(status, pageable);
  }

  public List<Purchase> getByType(OrderType type) {
    return purchaseRepository.findByType(type);
  }

  public Page<Purchase> getByType(OrderType type, Pageable pageable) {
    return purchaseRepository.findByType(type, pageable);
  }
}
