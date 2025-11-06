package com.example.kubico.infrastructure.web;

import com.example.kubico.domain.model.Favorite;
import com.example.kubico.domain.service.FavoriteService;
import com.example.kubico.infrastructure.dto.FavoriteCreateDTO;
import com.example.kubico.infrastructure.dto.FavoriteResponseDTO;
import com.example.kubico.infrastructure.dto.PageResponseDTO;
import com.example.kubico.infrastructure.mapper.FavoriteMapper;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
  public ResponseEntity<PageResponseDTO<FavoriteResponseDTO>> getAll(
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<Favorite> page = favoriteService.getAllFavorites(pageable);

    List<FavoriteResponseDTO> content =
        page.getContent().stream()
            .map(FavoriteMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<FavoriteResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<PageResponseDTO<FavoriteResponseDTO>> getByUser(
      @PathVariable UUID userId,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<Favorite> page = favoriteService.getFavoritesByUser(userId, pageable);

    List<FavoriteResponseDTO> content =
        page.getContent().stream()
            .map(FavoriteMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<FavoriteResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

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
