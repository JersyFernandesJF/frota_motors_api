package com.example.frotamotors.infrastructure.mapper;

import com.example.frotamotors.domain.model.Media;
import com.example.frotamotors.domain.model.Part;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.infrastructure.dto.MediaCreateDTO;
import com.example.frotamotors.infrastructure.dto.MediaResponseDTO;

public class MediaMapper {

  private MediaMapper() {}

  public static MediaResponseDTO toResponse(Media media) {
    return new MediaResponseDTO(
        media.getId(), media.getMediaType(), media.getUrl(), media.getUploadedAt());
  }

  public static Media toEntity(MediaCreateDTO dto) {
    Media media = new Media();
    media.setMediaType(dto.mediaType());
    media.setUrl(dto.url());
    return media;
  }

  public static Media toEntity(MediaCreateDTO dto, Vehicle vehicle, Part part) {
    Media media = new Media();
    media.setVehicle(vehicle);
    media.setPart(part);
    media.setMediaType(dto.mediaType());
    media.setUrl(dto.url());
    return media;
  }
}
