package com.example.kubico.infrastructure.web;

import com.example.kubico.domain.model.Favorite;
import com.example.kubico.domain.service.FavoriteService;
import com.example.kubico.infrastructure.dto.FavoriteCreateDTO;
import com.example.kubico.infrastructure.dto.FavoriteResponseDTO;
import com.example.kubico.infrastructure.mapper.FavoriteMapper;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/favorites")
@RequiredArgsConstructor
@Slf4j
public class FavoriteController {

  @Autowired private FavoriteService favoriteService;

  @Autowired
  public FavoriteController(FavoriteService favoriteService) {
    this.favoriteService = favoriteService;
  }

  @GetMapping
  public ResponseEntity<List<FavoriteResponseDTO>> getAll() {
    List<FavoriteResponseDTO> response =
        favoriteService.getAllFavorites().stream()
            .map(FavoriteMapper::toResponse)
            .collect(Collectors.toList());
    return ResponseEntity.ok(response);
  }

  @GetMapping("{id}")
  public ResponseEntity<FavoriteResponseDTO> getById(@PathVariable UUID id) {
    Favorite favorite = favoriteService.getFavoriteById(id);
    return ResponseEntity.ok(FavoriteMapper.toResponse(favorite));
  }

  @PostMapping
  public ResponseEntity<FavoriteResponseDTO> create(@RequestBody FavoriteCreateDTO dto) {
    Favorite favorite = favoriteService.createFavorite(dto);
    return ResponseEntity.ok(FavoriteMapper.toResponse(favorite));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    favoriteService.deleteFavorite(id);
    return ResponseEntity.noContent().build();
  }
}
