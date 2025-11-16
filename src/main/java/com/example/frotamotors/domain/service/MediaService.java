package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.model.Media;
import com.example.frotamotors.domain.model.Part;
import com.example.frotamotors.domain.model.Property;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.infrastructure.dto.MediaCreateDTO;
import com.example.frotamotors.infrastructure.mapper.MediaMapper;
import com.example.frotamotors.infrastructure.persistence.MediaRepository;
import com.example.frotamotors.infrastructure.persistence.PartRepository;
import com.example.frotamotors.infrastructure.persistence.PropertyRepository;
import com.example.frotamotors.infrastructure.persistence.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MediaService {

  @Autowired private MediaRepository mediaRepository;

  @Autowired private FileStorageService fileStorageService;

  @Autowired private PropertyRepository propertyRepository;

  @Autowired private VehicleRepository vehicleRepository;

  @Autowired private PartRepository partRepository;

  public Media create(MediaCreateDTO dto) {
    Property property = null;
    Vehicle vehicle = null;
    Part part = null;

    if (dto.propertyId() != null) {
      property =
          propertyRepository
              .findById(dto.propertyId())
              .orElseThrow(() -> new EntityNotFoundException("Property not found"));
    }
    if (dto.vehicleId() != null) {
      vehicle =
          vehicleRepository
              .findById(dto.vehicleId())
              .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));
    }
    if (dto.partId() != null) {
      part =
          partRepository
              .findById(dto.partId())
              .orElseThrow(() -> new EntityNotFoundException("Part not found"));
    }

    Media media = MediaMapper.toEntity(dto, property, vehicle, part);
    return mediaRepository.save(media);
  }

  public Media createWithFile(MultipartFile file, MediaCreateDTO dto) throws IOException {
    String fileUrl = fileStorageService.storeFile(file);
    MediaCreateDTO dtoWithUrl =
        new MediaCreateDTO(
            dto.propertyId(), dto.vehicleId(), dto.partId(), dto.mediaType(), fileUrl);

    Property property = null;
    Vehicle vehicle = null;
    Part part = null;

    if (dto.propertyId() != null) {
      property =
          propertyRepository
              .findById(dto.propertyId())
              .orElseThrow(() -> new EntityNotFoundException("Property not found"));
    }
    if (dto.vehicleId() != null) {
      vehicle =
          vehicleRepository
              .findById(dto.vehicleId())
              .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));
    }
    if (dto.partId() != null) {
      part =
          partRepository
              .findById(dto.partId())
              .orElseThrow(() -> new EntityNotFoundException("Part not found"));
    }

    Media media = MediaMapper.toEntity(dtoWithUrl, property, vehicle, part);
    return mediaRepository.save(media);
  }

  public List<Media> getAll() {
    return mediaRepository.findAll();
  }

  public Page<Media> getAll(Pageable pageable) {
    return mediaRepository.findAll(pageable);
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
    Media media =
        mediaRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Media not found"));

    // Delete file from storage
    try {
      fileStorageService.deleteFile(media.getUrl());
    } catch (IOException e) {
      // Log error but continue with deletion
      System.err.println("Failed to delete file: " + media.getUrl());
    }

    mediaRepository.deleteById(id);
  }
}
