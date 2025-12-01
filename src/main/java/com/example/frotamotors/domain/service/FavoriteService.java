package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.model.Favorite;
import com.example.frotamotors.infrastructure.dto.FavoriteCreateDTO;
import com.example.frotamotors.infrastructure.mapper.FavoriteMapper;
import com.example.frotamotors.infrastructure.persistence.FavoriteRepository;
import com.example.frotamotors.infrastructure.persistence.PartRepository;
import com.example.frotamotors.infrastructure.persistence.UserRepository;
import com.example.frotamotors.infrastructure.persistence.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

  @Autowired private FavoriteRepository favoriteRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private VehicleRepository vehicleRepository;

  @Autowired private PartRepository partRepository;

  public Favorite createFavorite(FavoriteCreateDTO dto) {
    var user =
        userRepository
            .findById(dto.userId())
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

    // Validate that exactly one entity is provided
    int entityCount = 0;
    var vehicle =
        dto.vehicleId() != null
            ? vehicleRepository
                .findById(dto.vehicleId())
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"))
            : null;
    if (vehicle != null) entityCount++;

    var part =
        dto.partId() != null
            ? partRepository
                .findById(dto.partId())
                .orElseThrow(() -> new EntityNotFoundException("Part not found"))
            : null;
    if (part != null) entityCount++;

    if (entityCount != 1) {
      throw new IllegalArgumentException(
          "Exactly one entity (vehicle or part) must be provided");
    }

    Favorite favorite = FavoriteMapper.toEntity(dto, user, vehicle, part);
    return favoriteRepository.save(favorite);
  }

  public List<Favorite> getAllFavorites() {
    return favoriteRepository.findAll();
  }

  public Page<Favorite> getAllFavorites(Pageable pageable) {
    return favoriteRepository.findAll(pageable);
  }

  public Page<Favorite> getFavoritesByUser(UUID userId, Pageable pageable) {
    return favoriteRepository.findByUserId(userId, pageable);
  }

  public Favorite getFavoriteById(UUID id) {
    return favoriteRepository
        .findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Favorite not found"));
  }

  public void deleteFavorite(UUID id) {
    if (!favoriteRepository.existsById(id)) {
      throw new EntityNotFoundException("Favorite not found");
    }
    favoriteRepository.deleteById(id);
  }
}
