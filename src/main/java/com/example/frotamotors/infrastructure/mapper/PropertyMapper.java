package com.example.frotamotors.infrastructure.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.example.frotamotors.domain.model.Media;
import com.example.frotamotors.domain.model.Property;
import com.example.frotamotors.infrastructure.dto.MediaResponseDTO;
import com.example.frotamotors.infrastructure.dto.PropertyCreateDTO;
import com.example.frotamotors.infrastructure.dto.PropertyResponseDTO;
import com.example.frotamotors.infrastructure.dto.PropertySummaryDTO;

public class PropertyMapper {

  private PropertyMapper() {}

  public static MediaResponseDTO toMediaResponseDTO(Media media) {
    if (media == null) return null;

    return new MediaResponseDTO(
        media.getId(), media.getMediaType(), media.getUrl(), media.getUploadedAt());
  }

  public static PropertySummaryDTO toSummary(Property property) {
    // Extract first image URL as thumbnail
    String thumbnailUrl = null;
    if (property.getMedia() != null && !property.getMedia().isEmpty()) {
      Media firstMedia = property.getMedia().get(0);
      if (firstMedia != null && firstMedia.getUrl() != null) {
        thumbnailUrl = firstMedia.getUrl();
      }
    }

    return new PropertySummaryDTO(
        property.getId(),
        property.getTitle(),
        property.getType(),
        property.getStatus(),
        property.getPrice(),
        property.getCurrency(),
        property.getAreaM2(),
        property.getRooms(),
        property.getBathrooms(),
        property.getYearBuilt(),
        thumbnailUrl,
        property.getCreatedAt());
  }

  public static PropertyResponseDTO toResponse(Property property) {
    List<MediaResponseDTO> mediaList = null;

    if (property.getMedia() != null && !property.getMedia().isEmpty()) {
      mediaList =
          property.getMedia().stream()
              .map(PropertyMapper::toMediaResponseDTO)
              .collect(Collectors.toList());
    }

    return new PropertyResponseDTO(
        property.getId(),
        property.getOwner() != null ? UserMapper.toResponse(property.getOwner()) : null,
        property.getAgency() != null ? AgencyMapper.toResponse(property.getAgency()) : null,
        property.getTitle(),
        property.getDescription(),
        property.getType(),
        property.getStatus(),
        property.getPrice(),
        property.getCurrency(),
        property.getAreaM2(),
        property.getRooms(),
        property.getBathrooms(),
        property.getFloor(),
        property.getTotalFloors(),
        property.getYearBuilt(),
        property.getEnergyCertificate(),
        mediaList, // aqui usamos a lista convertida, ou null se não houver medias
        property.getCreatedAt(),
        property.getUpdatedAt());
  }

  public static PropertyResponseDTO toEntity(PropertyCreateDTO dto) {
    Property property = new Property();
    property.setTitle(dto.title());
    property.setDescription(dto.description());
    property.setType(dto.type());
    property.setStatus(dto.status());
    property.setPrice(BigDecimal.valueOf(dto.price()));
    property.setCurrency(dto.currency());
    property.setAreaM2(dto.areaM2());
    property.setRooms(dto.rooms());
    property.setBathrooms(dto.bathrooms());
    property.setFloor(dto.floor());
    property.setTotalFloors(dto.totalFloors());
    property.setYearBuilt(dto.yearBuilt());
    property.setEnergyCertificate(dto.energyCertificate());
    return PropertyMapper.toResponse(property);
  }
}
