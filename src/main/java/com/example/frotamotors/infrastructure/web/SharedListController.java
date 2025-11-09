package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.service.SharedListService;
import com.example.frotamotors.infrastructure.dto.SharedListCreateDTO;
import com.example.frotamotors.infrastructure.dto.SharedListResponseDTO;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/shared-lists")
@RequiredArgsConstructor
@Slf4j
public class SharedListController {

  @Autowired private SharedListService sharedListService;

  @GetMapping
  public ResponseEntity<List<SharedListResponseDTO>> getAll() {
    return ResponseEntity.ok(sharedListService.getAll());
  }

  @GetMapping("{id}")
  public ResponseEntity<SharedListResponseDTO> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(sharedListService.getById(id));
  }

  @PostMapping
  public ResponseEntity<SharedListResponseDTO> create(@RequestBody SharedListCreateDTO dto) {
    return ResponseEntity.ok(sharedListService.create(dto));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    sharedListService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
