package com.example.kubico.infrastructure.web;

import com.example.kubico.domain.service.SharedListItemService;
import com.example.kubico.infrastructure.dto.SharedListItemCreateDTO;
import com.example.kubico.infrastructure.dto.SharedListItemResponseDTO;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/shared-list-items")
@RequiredArgsConstructor
@Slf4j
public class SharedListItemController {

  @Autowired private SharedListItemService sharedListItemService;

  @GetMapping
  public ResponseEntity<List<SharedListItemResponseDTO>> getAll() {
    return ResponseEntity.ok(sharedListItemService.getAll());
  }

  @GetMapping("{id}")
  public ResponseEntity<SharedListItemResponseDTO> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(sharedListItemService.getById(id));
  }

  @PostMapping
  public ResponseEntity<SharedListItemResponseDTO> create(
      @RequestBody SharedListItemCreateDTO dto) {
    return ResponseEntity.ok(sharedListItemService.create(dto));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    sharedListItemService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
