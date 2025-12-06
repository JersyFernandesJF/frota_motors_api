package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.enums.PartCategory;
import com.example.frotamotors.domain.enums.PartStatus;
import com.example.frotamotors.domain.model.Agency;
import com.example.frotamotors.domain.model.Part;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.infrastructure.dto.PartCreateDTO;
import com.example.frotamotors.infrastructure.mapper.PartMapper;
import com.example.frotamotors.infrastructure.persistence.AgencyRepository;
import com.example.frotamotors.infrastructure.persistence.PartRepository;
import com.example.frotamotors.infrastructure.persistence.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PartService {

  @Autowired private PartRepository partRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private AgencyRepository agencyRepository;

  public Part create(PartCreateDTO dto) {
    User seller =
        userRepository
            .findById(dto.sellerId())
            .orElseThrow(() -> new EntityNotFoundException("Seller not found"));

    Agency agency = null;
    if (dto.agencyId() != null) {
      agency =
          agencyRepository
              .findById(dto.agencyId())
              .orElseThrow(() -> new EntityNotFoundException("Agency not found"));
    }

    Part part = PartMapper.toEntity(dto, seller, agency);
    return partRepository.save(part);
  }

  public List<Part> getAll() {
    return partRepository.findAll();
  }

  public Page<Part> getAll(Pageable pageable) {
    return partRepository.findAll(pageable);
  }

  public Part getById(UUID id) {
    return partRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Part not found"));
  }

  public Part update(UUID id, PartCreateDTO dto) {
    Part existing =
        partRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Part not found"));

    User seller =
        userRepository
            .findById(dto.sellerId())
            .orElseThrow(() -> new EntityNotFoundException("Seller not found"));

    Agency agency = null;
    if (dto.agencyId() != null) {
      agency =
          agencyRepository
              .findById(dto.agencyId())
              .orElseThrow(() -> new EntityNotFoundException("Agency not found"));
    }

    existing.setSeller(seller);
    existing.setAgency(agency);
    existing.setCategory(dto.category());
    existing.setStatus(dto.status());
    existing.setName(dto.name());
    existing.setDescription(dto.description());
    existing.setPrice(BigDecimal.valueOf(dto.price()));
    existing.setCurrency(dto.currency().name());
    existing.setPartNumber(dto.partNumber());
    existing.setOemNumber(dto.oemNumber());
    existing.setBrand(dto.brand());
    existing.setCompatibleVehicles(dto.compatibleVehicles());
    existing.setConditionType(dto.conditionType());
    existing.setQuantityAvailable(dto.quantityAvailable());
    existing.setWarrantyMonths(dto.warrantyMonths());

    return partRepository.save(existing);
  }

  public void delete(UUID id) {
    if (!partRepository.existsById(id)) {
      throw new EntityNotFoundException("Part not found");
    }
    partRepository.deleteById(id);
  }

  public List<Part> search(
      PartCategory category,
      PartStatus status,
      BigDecimal minPrice,
      BigDecimal maxPrice,
      String brand,
      String name,
      String partNumber,
      String oemNumber) {
    return partRepository.search(
        category != null ? category.name() : null,
        status != null ? status.name() : null,
        minPrice,
        maxPrice,
        brand,
        name,
        partNumber,
        oemNumber);
  }

  public Page<Part> search(
      PartCategory category,
      PartStatus status,
      BigDecimal minPrice,
      BigDecimal maxPrice,
      String brand,
      String name,
      String partNumber,
      String oemNumber,
      Pageable pageable) {
    // Validate price range
    if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
      throw new IllegalArgumentException("minPrice cannot be greater than maxPrice");
    }
    return partRepository.searchPageable(
        category != null ? category.name() : null,
        status != null ? status.name() : null,
        minPrice,
        maxPrice,
        brand,
        name,
        partNumber,
        oemNumber,
        pageable);
  }

  public List<Part> getBySeller(UUID sellerId) {
    return partRepository.findBySellerId(sellerId);
  }

  public Page<Part> getBySeller(UUID sellerId, Pageable pageable) {
    return partRepository.findBySellerId(sellerId, pageable);
  }

  public List<Part> getByAgency(UUID agencyId) {
    return partRepository.findByAgencyId(agencyId);
  }

  public Page<Part> getByAgency(UUID agencyId, Pageable pageable) {
    return partRepository.findByAgencyId(agencyId, pageable);
  }

  public List<Part> getByCategory(PartCategory category) {
    return partRepository.findByCategory(category);
  }

  public Page<Part> getByCategory(PartCategory category, Pageable pageable) {
    return partRepository.findByCategory(category, pageable);
  }

  public List<Part> getByStatus(PartStatus status) {
    return partRepository.findByStatus(status);
  }

  public Page<Part> getByStatus(PartStatus status, Pageable pageable) {
    return partRepository.findByStatus(status, pageable);
  }
}
