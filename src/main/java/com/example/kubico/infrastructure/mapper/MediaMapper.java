package com.example.kubico.infrastructure.mapper;

import com.example.kubico.domain.model.Media;
import com.example.kubico.infrastructure.dto.MediaCreateDTO;
import com.example.kubico.infrastructure.dto.MediaResponseDTO;

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
}
