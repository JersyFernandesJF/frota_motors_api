package com.example.kubico.domain.service;

import com.example.kubico.domain.model.Media;
import com.example.kubico.infrastructure.dto.MediaCreateDTO;
import com.example.kubico.infrastructure.mapper.MediaMapper;
import com.example.kubico.infrastructure.persistence.MediaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MediaService {

  @Autowired private MediaRepository mediaRepository;

  public Media create(MediaCreateDTO dto) {
    Media media = MediaMapper.toEntity(dto);
    return mediaRepository.save(media);
  }

  public List<Media> getAll() {
    return mediaRepository.findAll();
  }

  public Media getById(UUID id) {
    return mediaRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Media not found"));
  }

  public Media update(UUID id, MediaCreateDTO dto) {
    Media existing =
        mediaRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Media not found"));

    existing.setUrl(dto.url());
    existing.setMediaType(dto.mediaType());

    return mediaRepository.save(existing);
  }

  public void delete(UUID id) {
    if (!mediaRepository.existsById(id)) {
      throw new EntityNotFoundException("Media not found");
    }
    mediaRepository.deleteById(id);
  }
}
