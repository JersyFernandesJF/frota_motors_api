package com.example.kubico.infrastructure.web;

import com.example.kubico.domain.model.Property;
import com.example.kubico.domain.service.PropertyService;
import com.example.kubico.infrastructure.dto.PropertyResponseDTO;
import com.example.kubico.infrastructure.mapper.PropertyMapper;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/properties")
@RequiredArgsConstructor
@Slf4j
public class PropertyController {

  @Autowired private PropertyService propertyService;

  @GetMapping("/search")
  public ResponseEntity<List<PropertyResponseDTO>> search(
      @RequestParam(required = false) Double minArea,
      @RequestParam(required = false) Double maxArea,
      @RequestParam(required = false) List<String> types,
      @RequestParam(required = false) Integer bathrooms,
      @RequestParam(required = false) Integer rooms,
      @RequestParam(required = false) Integer floors,
      @RequestParam(required = false) Integer year) {

    List<PropertyResponseDTO> response =
        propertyService.search(minArea, maxArea, types, bathrooms, rooms, floors, year).stream()
            .map(PropertyMapper::toResponse)
            .collect(Collectors.toList());

    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<PropertyResponseDTO>> getAll() {
    List<PropertyResponseDTO> response =
        propertyService.getAll().stream()
            .map(PropertyMapper::toResponse)
            .collect(Collectors.toList());
    return ResponseEntity.ok(response);
  }

  @GetMapping("{id}")
  public ResponseEntity<PropertyResponseDTO> getById(@PathVariable UUID id) {
    Property property = propertyService.getById(id);
    return ResponseEntity.ok(PropertyMapper.toResponse(property));
  }

  @PostMapping
  public ResponseEntity<PropertyResponseDTO> create(@RequestBody Property property) {
    Property saved = propertyService.create(property);
    return ResponseEntity.ok(PropertyMapper.toResponse(saved));
  }

  @PutMapping("{id}")
  public ResponseEntity<PropertyResponseDTO> update(
      @PathVariable UUID id, @RequestBody Property property) {
    Property updated = propertyService.update(id, property);
    return ResponseEntity.ok(PropertyMapper.toResponse(updated));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    propertyService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
