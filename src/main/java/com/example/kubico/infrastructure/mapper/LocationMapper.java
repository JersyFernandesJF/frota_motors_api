package com.example.kubico.infrastructure.mapper;

import com.example.kubico.domain.model.Location;
import com.example.kubico.infrastructure.dto.LocationCreateDTO;
import com.example.kubico.infrastructure.dto.LocationResponseDTO;

public class LocationMapper {

  private LocationMapper() {}

  public static LocationResponseDTO toResponse(Location location) {
    return new LocationResponseDTO(
        location.getId(),
        location.getAddress(),
        location.getCity(),
        location.getDistrict(),
        location.getPostalCode(),
        location.getLatitude(),
        location.getLongitude());
  }

  public static Location toEntity(LocationCreateDTO dto) {
    Location location = new Location();
    location.setAddress(dto.address());
    location.setCity(dto.city());
    location.setDistrict(dto.district());
    location.setPostalCode(dto.postalCode());
    location.setLatitude(dto.latitude());
    location.setLongitude(dto.longitude());
    return location;
  }
}
