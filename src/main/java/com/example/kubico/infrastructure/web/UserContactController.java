package com.example.kubico.infrastructure.web;

import com.example.kubico.domain.service.UserContactService;
import com.example.kubico.infrastructure.dto.UserContactDTO;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user-contacts")
@RequiredArgsConstructor
@Slf4j
public class UserContactController {

  @Autowired private UserContactService userContactService;

  @GetMapping("{id}")
  public ResponseEntity<UserContactDTO> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(userContactService.getById(id));
  }

  @GetMapping("user/{userId}")
  public ResponseEntity<List<UserContactDTO>> getByUser(@PathVariable UUID userId) {
    return ResponseEntity.ok(userContactService.getByUser(userId));
  }

  @PostMapping("user/{userId}")
  public ResponseEntity<UserContactDTO> create(
      @PathVariable UUID userId, @RequestBody UserContactDTO dto) {
    return ResponseEntity.ok(userContactService.create(dto, userId));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    userContactService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
