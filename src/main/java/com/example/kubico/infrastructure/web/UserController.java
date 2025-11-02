package com.example.kubico.infrastructure.web;

import com.example.kubico.domain.model.User;
import com.example.kubico.domain.service.UserService;
import com.example.kubico.infrastructure.dto.UserCreateDTO;
import com.example.kubico.infrastructure.dto.UserResponseDTO;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

  @Autowired private UserService userService;

  @GetMapping("{id}")
  public ResponseEntity<User> getUserById(@PathVariable UUID id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @PostMapping
  public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserCreateDTO dto) {
    return ResponseEntity.ok(userService.createUser(dto));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
