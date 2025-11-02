package com.example.kubico.infrastructure.web;

import com.example.kubico.domain.model.Media;
import com.example.kubico.domain.service.MediaService;
import com.example.kubico.infrastructure.dto.MediaCreateDTO;
import com.example.kubico.infrastructure.dto.MediaResponseDTO;
import com.example.kubico.infrastructure.mapper.MediaMapper;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/media")
@RequiredArgsConstructor
@Slf4j
public class MediaController {

  @Autowired private MediaService mediaService;

  @GetMapping
  public ResponseEntity<List<MediaResponseDTO>> getAll() {
    List<MediaResponseDTO> response =
        mediaService.getAll().stream().map(MediaMapper::toResponse).collect(Collectors.toList());
    return ResponseEntity.ok(response);
  }

  @GetMapping("{id}")
  public ResponseEntity<MediaResponseDTO> getById(@PathVariable UUID id) {
    Media media = mediaService.getById(id);
    return ResponseEntity.ok(MediaMapper.toResponse(media));
  }

  @PostMapping
  public ResponseEntity<MediaResponseDTO> create(@RequestBody MediaCreateDTO dto) {
    Media media = mediaService.create(dto);
    return ResponseEntity.ok(MediaMapper.toResponse(media));
  }

  @PutMapping("{id}")
  public ResponseEntity<MediaResponseDTO> update(
      @PathVariable UUID id, @RequestBody MediaCreateDTO dto) {
    Media media = mediaService.update(id, dto);
    return ResponseEntity.ok(MediaMapper.toResponse(media));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    mediaService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
