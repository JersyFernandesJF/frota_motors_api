package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.enums.PartCategory;
import com.example.frotamotors.domain.enums.PartStatus;
import com.example.frotamotors.domain.model.Part;
import com.example.frotamotors.domain.service.PartService;
import com.example.frotamotors.infrastructure.dto.PageResponseDTO;
import com.example.frotamotors.infrastructure.dto.PartCreateDTO;
import com.example.frotamotors.infrastructure.dto.PartResponseDTO;
import com.example.frotamotors.infrastructure.mapper.PartMapper;
import java.math.BigDecimal;
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
@RequestMapping("api/v1/parts")
@RequiredArgsConstructor
@Slf4j
public class PartController {

  @Autowired private PartService partService;

  @GetMapping("/search")
  public ResponseEntity<PageResponseDTO<PartResponseDTO>> search(
      @RequestParam(required = false) PartCategory category,
      @RequestParam(required = false) PartStatus status,
      @RequestParam(required = false) BigDecimal minPrice,
      @RequestParam(required = false) BigDecimal maxPrice,
      @RequestParam(required = false) String brand,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String partNumber,
      @RequestParam(required = false) String oemNumber,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {

    Page<Part> page =
        partService.search(
            category, status, minPrice, maxPrice, brand, name, partNumber, oemNumber, pageable);

    List<PartResponseDTO> content =
        page.getContent().stream().map(PartMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<PartResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<PageResponseDTO<PartResponseDTO>> getAll(
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Part> page = partService.getAll(pageable);

    List<PartResponseDTO> content =
        page.getContent().stream().map(PartMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<PartResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/seller/{sellerId}")
  public ResponseEntity<PageResponseDTO<PartResponseDTO>> getBySeller(
      @PathVariable UUID sellerId,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Part> page = partService.getBySeller(sellerId, pageable);

    List<PartResponseDTO> content =
        page.getContent().stream().map(PartMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<PartResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/agency/{agencyId}")
  public ResponseEntity<PageResponseDTO<PartResponseDTO>> getByAgency(
      @PathVariable UUID agencyId,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Part> page = partService.getByAgency(agencyId, pageable);

    List<PartResponseDTO> content =
        page.getContent().stream().map(PartMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<PartResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/category/{category}")
  public ResponseEntity<PageResponseDTO<PartResponseDTO>> getByCategory(
      @PathVariable PartCategory category,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Part> page = partService.getByCategory(category, pageable);

    List<PartResponseDTO> content =
        page.getContent().stream().map(PartMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<PartResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/status/{status}")
  public ResponseEntity<PageResponseDTO<PartResponseDTO>> getByStatus(
      @PathVariable PartStatus status,
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Part> page = partService.getByStatus(status, pageable);

    List<PartResponseDTO> content =
        page.getContent().stream().map(PartMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<PartResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("{id}")
  public ResponseEntity<PartResponseDTO> getById(@PathVariable UUID id) {
    Part part = partService.getById(id);
    return ResponseEntity.ok(PartMapper.toResponse(part));
  }

  @PostMapping
  public ResponseEntity<PartResponseDTO> create(@RequestBody PartCreateDTO dto) {
    Part saved = partService.create(dto);
    return ResponseEntity.ok(PartMapper.toResponse(saved));
  }

  @PutMapping("{id}")
  public ResponseEntity<PartResponseDTO> update(
      @PathVariable UUID id, @RequestBody PartCreateDTO dto) {
    Part updated = partService.update(id, dto);
    return ResponseEntity.ok(PartMapper.toResponse(updated));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    partService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
