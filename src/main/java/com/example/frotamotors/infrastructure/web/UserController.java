package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.enums.Role;
import com.example.frotamotors.domain.enums.UserStatus;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.UserActivity;
import com.example.frotamotors.domain.service.UserService;
import com.example.frotamotors.infrastructure.dto.ExportRequestDTO;
import com.example.frotamotors.infrastructure.dto.PageResponseDTO;
import com.example.frotamotors.infrastructure.dto.UserActivityResponseDTO;
import com.example.frotamotors.infrastructure.dto.UserBanRequestDTO;
import com.example.frotamotors.infrastructure.dto.UserContactRequestDTO;
import com.example.frotamotors.infrastructure.dto.UserCreateDTO;
import com.example.frotamotors.infrastructure.dto.UserResponseDTO;
import com.example.frotamotors.infrastructure.dto.UserSuspendRequestDTO;
import com.example.frotamotors.infrastructure.dto.UserUpdateDTO;
import com.example.frotamotors.infrastructure.mapper.UserMapper;
import com.example.frotamotors.infrastructure.security.CustomUserDetailsService.CustomUserDetails;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

	@Autowired
	private UserService userService;

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
	public ResponseEntity<PageResponseDTO<UserResponseDTO>> getAllUsers(
			@RequestParam(required = false) Role role,
			@RequestParam(required = false) UserStatus status,
			@RequestParam(required = false) String search,
			@PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
		Page<User> page = userService.getAllUsers(pageable, role, status, search);

		List<UserResponseDTO> content = page.getContent().stream().map(UserMapper::toResponse)
				.collect(Collectors.toList());

		PageResponseDTO<UserResponseDTO> response = PageResponseDTO.of(content, page.getNumber(), page.getSize(),
				page.getTotalElements());

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

	@DeleteMapping("/me")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<Void> deleteCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			UUID userId = userDetails.getUserId();
			userService.deleteUser(userId);
			return ResponseEntity.noContent().build();
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
	@PreAuthorize("hasRole('ADMIN') or (hasAnyRole('BUYER', 'OWNER', 'AGENT') and @securityUtils.isCurrentUser(#id))")
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

	@PostMapping("{id}/suspend")
	@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
	public ResponseEntity<UserResponseDTO> suspendUser(
			@PathVariable UUID id, @Valid @RequestBody UserSuspendRequestDTO request) {
		User user = userService.suspendUser(id, request);
		return ResponseEntity.ok(UserMapper.toResponse(user));
	}

	@PostMapping("{id}/ban")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserResponseDTO> banUser(
			@PathVariable UUID id, @Valid @RequestBody UserBanRequestDTO request) {
		User user = userService.banUser(id, request);
		return ResponseEntity.ok(UserMapper.toResponse(user));
	}

	@PostMapping("{id}/reactivate")
	@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
	public ResponseEntity<UserResponseDTO> reactivateUser(@PathVariable UUID id) {
		User user = userService.reactivateUser(id);
		return ResponseEntity.ok(UserMapper.toResponse(user));
	}

	@PostMapping("{id}/contact")
	@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
	public ResponseEntity<Void> contactUser(
			@PathVariable UUID id, @Valid @RequestBody UserContactRequestDTO request) {
		userService.contactUser(id, request);
		return ResponseEntity.ok().build();
	}

	@GetMapping("{id}/activity-history")
	@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
	public ResponseEntity<PageResponseDTO<UserActivityResponseDTO>> getActivityHistory(
			@PathVariable UUID id,
			@PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
		Page<UserActivity> page = userService.getActivityHistory(id, pageable);

		ObjectMapper objectMapper = new ObjectMapper();
		List<UserActivityResponseDTO> content = page.getContent().stream()
				.map(
						activity -> {
							try {
								java.util.Map<String, Object> metadata = null;
								if (activity.getMetadata() != null && !activity.getMetadata().isEmpty()) {
									metadata = objectMapper.readValue(
											activity.getMetadata(),
											new TypeReference<java.util.Map<String, Object>>() {
											});
								}
								return new UserActivityResponseDTO(
										activity.getId(),
										activity.getType(),
										activity.getDescription(),
										activity.getRelatedEntityType(),
										activity.getRelatedEntityId(),
										metadata,
										activity.getCreatedAt());
							} catch (Exception e) {
								return new UserActivityResponseDTO(
										activity.getId(),
										activity.getType(),
										activity.getDescription(),
										activity.getRelatedEntityType(),
										activity.getRelatedEntityId(),
										null,
										activity.getCreatedAt());
							}
						})
				.collect(Collectors.toList());

		PageResponseDTO<UserActivityResponseDTO> response = PageResponseDTO.of(content, page.getNumber(),
				page.getSize(), page.getTotalElements());

		return ResponseEntity.ok(response);
	}

	@PostMapping("{id}/reset-password")
	@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
	public ResponseEntity<Void> resetPassword(
			@PathVariable UUID id,
			@Valid @RequestBody com.example.frotamotors.infrastructure.dto.AdminResetPasswordRequestDTO request) {
		userService.resetPassword(id, request);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/export")
	@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
	public ResponseEntity<java.util.Map<String, String>> exportUsers(
			@Valid @RequestBody ExportRequestDTO request) {
		String result = userService.exportUsers(request);
		return ResponseEntity.ok(java.util.Map.of("message", result));
	}
}
