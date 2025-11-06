package com.example.kubico.infrastructure.web;

import com.example.kubico.domain.model.User;
import com.example.kubico.domain.service.UserService;
import com.example.kubico.infrastructure.dto.PageResponseDTO;
import com.example.kubico.infrastructure.dto.UserCreateDTO;
import com.example.kubico.infrastructure.dto.UserResponseDTO;
import com.example.kubico.infrastructure.dto.UserUpdateDTO;
import com.example.kubico.infrastructure.mapper.UserMapper;
import com.example.kubico.infrastructure.security.CustomUserDetailsService.CustomUserDetails;
import jakarta.validation.Valid;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

  @Autowired private UserService userService;

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<PageResponseDTO<UserResponseDTO>> getAllUsers(
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<User> page = userService.getAllUsers(pageable);

    List<UserResponseDTO> content =
        page.getContent().stream()
            .map(UserMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<UserResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/me")
  public ResponseEntity<UserResponseDTO> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
      CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
      User user = userService.getUserById(userDetails.getUserId());
      return ResponseEntity.ok(UserMapper.toResponse(user));
    }
    return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
  }

  @GetMapping("{id}")
  public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
    User user = userService.getUserById(id);
    return ResponseEntity.ok(UserMapper.toResponse(user));
  }

  @PostMapping
  public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO dto) {
    return ResponseEntity.ok(userService.createUser(dto));
  }

  @PutMapping("{id}")
  @PreAuthorize("hasRole('ADMIN') or (hasAnyRole('BUYER', 'SELLER', 'AGENCY') and @securityUtils.isCurrentUser(#id))")
  public ResponseEntity<UserResponseDTO> updateUser(
      @PathVariable UUID id, @Valid @RequestBody UserUpdateDTO dto) {
    User updated = userService.updateUser(id, dto);
    return ResponseEntity.ok(UserMapper.toResponse(updated));
  }

  @DeleteMapping("{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}
