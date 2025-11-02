package com.example.kubico.infrastructure.web;

import com.example.kubico.domain.model.Location;
import com.example.kubico.domain.service.LocationService;
import com.example.kubico.infrastructure.dto.LocationCreateDTO;
import com.example.kubico.infrastructure.dto.LocationResponseDTO;
import com.example.kubico.infrastructure.mapper.LocationMapper;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/locations")
@RequiredArgsConstructor
@Slf4j
public class LocationController {

  @Autowired private LocationService locationService;

  @GetMapping
  public ResponseEntity<List<LocationResponseDTO>> getAll() {
    List<LocationResponseDTO> response =
        locationService.getAll().stream()
            .map(LocationMapper::toResponse)
            .collect(Collectors.toList());
    return ResponseEntity.ok(response);
  }

  @GetMapping("{id}")
  public ResponseEntity<LocationResponseDTO> getById(@PathVariable UUID id) {
    Location location = locationService.getById(id);
    return ResponseEntity.ok(LocationMapper.toResponse(location));
  }

  @PostMapping
  public ResponseEntity<LocationResponseDTO> create(@RequestBody LocationCreateDTO dto) {
    Location location = locationService.create(dto);
    return ResponseEntity.ok(LocationMapper.toResponse(location));
  }

  @PutMapping("{id}")
  public ResponseEntity<LocationResponseDTO> update(
      @PathVariable UUID id, @RequestBody LocationCreateDTO dto) {
    Location location = locationService.update(id, dto);
    return ResponseEntity.ok(LocationMapper.toResponse(location));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    locationService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
