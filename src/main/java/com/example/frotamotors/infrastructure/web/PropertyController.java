package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.model.Property;
import com.example.frotamotors.domain.service.PropertyService;
import com.example.frotamotors.infrastructure.dto.PageResponseDTO;
import com.example.frotamotors.infrastructure.dto.PropertyResponseDTO;
import com.example.frotamotors.infrastructure.mapper.PropertyMapper;
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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/v1/properties")
@RequiredArgsConstructor
@Slf4j
public class PropertyController {

  @Autowired private PropertyService propertyService;

  @GetMapping("/search")
  public ResponseEntity<PageResponseDTO<PropertyResponseDTO>> search(
      @RequestParam(required = false) Double minArea,
      @RequestParam(required = false) Double maxArea,
      @RequestParam(required = false) List<String> types,
      @RequestParam(required = false) Integer bathrooms,
      @RequestParam(required = false) Integer rooms,
      @RequestParam(required = false) Integer floors,
      @RequestParam(required = false) Integer year,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {

    Page<Property> page =
        propertyService.search(minArea, maxArea, types, bathrooms, rooms, floors, year, pageable);

    List<PropertyResponseDTO> content =
        page.getContent().stream().map(PropertyMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<PropertyResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<PageResponseDTO<PropertyResponseDTO>> getAll(
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Property> page = propertyService.getAll(pageable);

    List<PropertyResponseDTO> content =
        page.getContent().stream().map(PropertyMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<PropertyResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

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
