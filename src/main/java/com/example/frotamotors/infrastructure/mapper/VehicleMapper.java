package com.example.frotamotors.infrastructure.mapper;

import com.example.frotamotors.domain.model.Agency;
import com.example.frotamotors.domain.model.Media;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.infrastructure.dto.MediaResponseDTO;
import com.example.frotamotors.infrastructure.dto.VehicleCreateDTO;
import com.example.frotamotors.infrastructure.dto.VehicleResponseDTO;
import com.example.frotamotors.infrastructure.dto.VehicleSummaryDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class VehicleMapper {

  private VehicleMapper() {}

  public static MediaResponseDTO toMediaResponseDTO(Media media) {
    if (media == null) return null;

    return new MediaResponseDTO(
        media.getId(), media.getMediaType(), media.getUrl(), media.getUploadedAt());
  }

  public static VehicleSummaryDTO toSummary(Vehicle vehicle) {
    // Extract first image URL as thumbnail
    String thumbnailUrl = null;
    if (vehicle.getMedia() != null && !vehicle.getMedia().isEmpty()) {
      Media firstMedia = vehicle.getMedia().get(0);
      if (firstMedia != null && firstMedia.getUrl() != null) {
        thumbnailUrl = firstMedia.getUrl();
      }
    }

    return new VehicleSummaryDTO(
        vehicle.getId(),
        vehicle.getType(),
        vehicle.getStatus(),
        vehicle.getBrand(),
        vehicle.getModel(),
        vehicle.getYear(),
        vehicle.getColor(),
        vehicle.getMileageKm(),
        vehicle.getPrice(),
        vehicle.getCurrency(),
        vehicle.getFuelType(),
        vehicle.getTransmissionType(),
        vehicle.getEngineSize(),
        vehicle.getHorsePower(),
        thumbnailUrl,
        vehicle.getCreatedAt());
  }

  public static VehicleResponseDTO toResponse(Vehicle vehicle) {
    List<MediaResponseDTO> mediaList = null;

    if (vehicle.getMedia() != null && !vehicle.getMedia().isEmpty()) {
      mediaList =
          vehicle.getMedia().stream()
              .map(VehicleMapper::toMediaResponseDTO)
              .collect(Collectors.toList());
    }

    return new VehicleResponseDTO(
        vehicle.getId(),
        vehicle.getOwner() != null ? UserMapper.toResponse(vehicle.getOwner()) : null,
        vehicle.getAgency() != null ? AgencyMapper.toResponse(vehicle.getAgency()) : null,
        vehicle.getType(),
        vehicle.getStatus(),
        vehicle.getBrand(),
        vehicle.getModel(),
        vehicle.getYear(),
        vehicle.getColor(),
        vehicle.getLicensePlate(),
        vehicle.getVin(),
        vehicle.getMileageKm(),
        vehicle.getPrice(),
        vehicle.getCurrency(),
        vehicle.getDescription(),
        vehicle.getFuelType(),
        vehicle.getTransmissionType(),
        vehicle.getEngineSize(),
        vehicle.getHorsePower(),
        vehicle.getNumberOfDoors(),
        vehicle.getNumberOfSeats(),
        vehicle.getPreviousOwners(),
        vehicle.getAccidentHistory(),
        vehicle.getModerationStatus(),
        vehicle.getPublishedAt(),
        vehicle.getViews(),
        vehicle.getFavoritesCount(),
        vehicle.getMessagesCount(),
        mediaList,
        vehicle.getCreatedAt(),
        vehicle.getUpdatedAt());
  }

  public static Vehicle toEntity(VehicleCreateDTO dto, User owner, Agency agency) {
    Vehicle vehicle = new Vehicle();
    vehicle.setOwner(owner);
    vehicle.setAgency(agency);
    vehicle.setType(dto.type());
    vehicle.setStatus(dto.status());
    vehicle.setBrand(dto.brand());
    vehicle.setModel(dto.model());
    vehicle.setYear(dto.year());
    vehicle.setColor(dto.color());
    vehicle.setLicensePlate(dto.licensePlate());
    vehicle.setVin(dto.vin());
    vehicle.setMileageKm(dto.mileageKm());
    vehicle.setPrice(BigDecimal.valueOf(dto.price()));
    vehicle.setCurrency(dto.currency().name());
    vehicle.setDescription(dto.description());
    vehicle.setFuelType(dto.fuelType());
    vehicle.setTransmissionType(dto.transmissionType());
    vehicle.setEngineSize(dto.engineSize());
    vehicle.setHorsePower(dto.horsePower());
    vehicle.setNumberOfDoors(dto.numberOfDoors());
    vehicle.setNumberOfSeats(dto.numberOfSeats());
    vehicle.setPreviousOwners(dto.previousOwners());
    vehicle.setAccidentHistory(dto.accidentHistory());
    return vehicle;
  }
}
