package com.example.kubico.infrastructure.mapper;

import com.example.kubico.domain.model.Favorite;
import com.example.kubico.domain.model.Part;
import com.example.kubico.domain.model.Property;
import com.example.kubico.domain.model.User;
import com.example.kubico.domain.model.Vehicle;
import com.example.kubico.infrastructure.dto.FavoriteCreateDTO;
import com.example.kubico.infrastructure.dto.FavoriteResponseDTO;

public class FavoriteMapper {
  private FavoriteMapper() {}

  public static FavoriteResponseDTO toResponse(Favorite favorite) {
    return new FavoriteResponseDTO(
        favorite.getId(),
        favorite.getUser().getId(),
        favorite.getProperty() != null ? favorite.getProperty().getId() : null,
        favorite.getVehicle() != null ? favorite.getVehicle().getId() : null,
        favorite.getPart() != null ? favorite.getPart().getId() : null,
        favorite.getCreatedAt());
  }

  public static Favorite toEntity(
      FavoriteCreateDTO dto, User user, Property property, Vehicle vehicle, Part part) {
    Favorite favorite = new Favorite();
    favorite.setUser(user);
    favorite.setProperty(property);
    favorite.setVehicle(vehicle);
    favorite.setPart(part);
    return favorite;
  }
}
