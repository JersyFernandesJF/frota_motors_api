package com.example.kubico.infrastructure.web;

import com.example.kubico.domain.service.SharedListMemberService;
import com.example.kubico.infrastructure.dto.SharedListMemberCreateDTO;
import com.example.kubico.infrastructure.dto.SharedListMemberResponseDTO;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/shared-list-members")
@RequiredArgsConstructor
@Slf4j
public class SharedListMemberController {

  @Autowired private SharedListMemberService sharedListMemberService;

  @GetMapping
  public ResponseEntity<List<SharedListMemberResponseDTO>> getAll() {
    return ResponseEntity.ok(sharedListMemberService.getAll());
  }

  @GetMapping("{id}")
  public ResponseEntity<SharedListMemberResponseDTO> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(sharedListMemberService.getById(id));
  }

  @PostMapping
  public ResponseEntity<SharedListMemberResponseDTO> create(
      @RequestBody SharedListMemberCreateDTO dto) {
    return ResponseEntity.ok(sharedListMemberService.create(dto));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    sharedListMemberService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
