package com.example.frotamotors.infrastructure.mapper;

import com.example.frotamotors.domain.model.Favorite;
import com.example.frotamotors.domain.model.Part;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.infrastructure.dto.FavoriteCreateDTO;
import com.example.frotamotors.infrastructure.dto.FavoriteResponseDTO;

public class FavoriteMapper {
  private FavoriteMapper() {}

  public static FavoriteResponseDTO toResponse(Favorite favorite) {
    return new FavoriteResponseDTO(
        favorite.getId(),
        favorite.getUser().getId(),
        null,
        favorite.getVehicle() != null ? favorite.getVehicle().getId() : null,
        favorite.getPart() != null ? favorite.getPart().getId() : null,
        favorite.getCreatedAt());
  }

  public static Favorite toEntity(
      FavoriteCreateDTO dto, User user, Vehicle vehicle, Part part) {
    Favorite favorite = new Favorite();
    favorite.setUser(user);
    favorite.setVehicle(vehicle);
    favorite.setPart(part);
    return favorite;
  }
}
