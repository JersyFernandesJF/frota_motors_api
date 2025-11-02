package com.example.kubico.domain.service;

import com.example.kubico.domain.model.Location;
import com.example.kubico.infrastructure.dto.LocationCreateDTO;
import com.example.kubico.infrastructure.mapper.LocationMapper;
import com.example.kubico.infrastructure.persistence.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

  @Autowired private LocationRepository locationRepository;

  public Location create(LocationCreateDTO dto) {
    Location location = LocationMapper.toEntity(dto);
    return locationRepository.save(location);
  }

  public List<Location> getAll() {
    return locationRepository.findAll();
  }

  public Location getById(UUID id) {
    return locationRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Location not found"));
  }

  public Location update(UUID id, LocationCreateDTO dto) {
    Location existing =
        locationRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Location not found"));

    existing.setAddress(dto.address());
    return locationRepository.save(existing);
  }

  public void delete(UUID id) {
    if (!locationRepository.existsById(id)) {
      throw new EntityNotFoundException("Location not found");
    }
    locationRepository.deleteById(id);
  }
}
