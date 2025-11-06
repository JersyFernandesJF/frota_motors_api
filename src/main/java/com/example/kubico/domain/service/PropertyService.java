package com.example.kubico.domain.service;

import com.example.kubico.domain.model.Property;
import com.example.kubico.infrastructure.persistence.PropertyRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {

  @Autowired private PropertyRepository propertyRepository;

  public Property create(Property property) {
    return propertyRepository.save(property);
  }

  public List<Property> getAll() {
    return propertyRepository.findAll();
  }

  public Page<Property> getAll(Pageable pageable) {
    return propertyRepository.findAll(pageable);
  }

  public Property getById(UUID id) {
    return propertyRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Property not found"));
  }

  public Property update(UUID id, Property property) {
    Property existing =
        propertyRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Property not found"));

    existing.setTitle(property.getTitle());
    existing.setDescription(property.getDescription());
    existing.setType(property.getType());
    existing.setStatus(property.getStatus());
    existing.setPrice(property.getPrice());
    existing.setCurrency(property.getCurrency());
    existing.setAreaM2(property.getAreaM2());
    existing.setRooms(property.getRooms());
    existing.setBathrooms(property.getBathrooms());
    existing.setFloor(property.getFloor());
    existing.setTotalFloors(property.getTotalFloors());
    existing.setYearBuilt(property.getYearBuilt());
    existing.setEnergyCertificate(property.getEnergyCertificate());

    return propertyRepository.save(existing);
  }

  public void delete(UUID id) {
    if (!propertyRepository.existsById(id)) {
      throw new EntityNotFoundException("Property not found");
    }
    propertyRepository.deleteById(id);
  }

  public List<Property> search(
      Double minArea,
      Double maxArea,
      List<String> types,
      Integer bathrooms,
      Integer rooms,
      Integer floors,
      Integer year) {

    double min = (minArea != null) ? minArea : 0;
    double max = (maxArea != null) ? maxArea : Double.MAX_VALUE;
    List<String> propertyTypes =
        (types != null && !types.isEmpty()) ? types : List.of("FOR_RENT", "FOR_SALE");
    int minBathrooms = (bathrooms != null) ? bathrooms : 0;
    int minRooms = (rooms != null) ? rooms : 0;
    int minFloors = (floors != null) ? floors : 0;
    int minYear = (year != null) ? year : 0;

    return propertyRepository
        .findByAreaM2BetweenAndTypeInAndBathroomsGreaterThanEqualAndRoomsGreaterThanEqualAndTotalFloorsGreaterThanEqualAndYearBuiltGreaterThanEqual(
            min, max, propertyTypes, minBathrooms, minRooms, minFloors, minYear);
  }

  public Page<Property> search(
      Double minArea,
      Double maxArea,
      List<String> types,
      Integer bathrooms,
      Integer rooms,
      Integer floors,
      Integer year,
      Pageable pageable) {
    // Validate area range
    if (minArea != null && maxArea != null && minArea > maxArea) {
      throw new IllegalArgumentException("minArea cannot be greater than maxArea");
    }

    List<String> propertyTypes =
        (types != null && !types.isEmpty()) ? types : List.of("FOR_RENT", "FOR_SALE");

    return propertyRepository.searchPageable(
        minArea, maxArea, propertyTypes, bathrooms, rooms, floors, year, pageable);
  }
}
