package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.model.Media;
import com.example.frotamotors.domain.service.MediaService;
import com.example.frotamotors.infrastructure.dto.MediaCreateDTO;
import com.example.frotamotors.infrastructure.dto.MediaResponseDTO;
import com.example.frotamotors.infrastructure.dto.PageResponseDTO;
import com.example.frotamotors.infrastructure.mapper.MediaMapper;
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/media")
@RequiredArgsConstructor
@Slf4j
public class MediaController {

  @Autowired private MediaService mediaService;

  @GetMapping
  public ResponseEntity<PageResponseDTO<MediaResponseDTO>> getAll(
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<Media> page = mediaService.getAll(pageable);

    List<MediaResponseDTO> content =
        page.getContent().stream()
            .map(MediaMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<MediaResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

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

  @PostMapping("/upload")
  public ResponseEntity<MediaResponseDTO> uploadFile(
      @RequestParam("file") MultipartFile file,
      @RequestParam(required = false) UUID propertyId,
      @RequestParam(required = false) UUID vehicleId,
      @RequestParam(required = false) UUID partId,
      @RequestParam com.example.frotamotors.domain.enums.MediaType mediaType)
      throws IOException {
    MediaCreateDTO dto = new MediaCreateDTO(propertyId, vehicleId, partId, mediaType, null);
    Media media = mediaService.createWithFile(file, dto);
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
