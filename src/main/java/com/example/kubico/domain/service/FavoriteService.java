package com.example.kubico.domain.service;

import com.example.kubico.domain.model.Favorite;
import com.example.kubico.infrastructure.dto.FavoriteCreateDTO;
import com.example.kubico.infrastructure.mapper.FavoriteMapper;
import com.example.kubico.infrastructure.persistence.FavoriteRepository;
import com.example.kubico.infrastructure.persistence.PropertyRepository;
import com.example.kubico.infrastructure.persistence.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

  @Autowired private FavoriteRepository favoriteRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private PropertyRepository propertyRepository;

  public Favorite createFavorite(FavoriteCreateDTO dto) {
    var user =
        userRepository
            .findById(dto.userId())
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    var property =
        propertyRepository
            .findById(dto.propertyId())
            .orElseThrow(() -> new EntityNotFoundException("Property not found"));

    Favorite favorite = FavoriteMapper.toEntity(dto, user, property);
    return favoriteRepository.save(favorite);
  }

  public List<Favorite> getAllFavorites() {
    return favoriteRepository.findAll();
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
