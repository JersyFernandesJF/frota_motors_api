package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.service.AuthService;
import com.example.frotamotors.domain.service.UserService;
import com.example.frotamotors.infrastructure.dto.AuthResponseDTO;
import com.example.frotamotors.infrastructure.dto.ForgotPasswordRequestDTO;
import com.example.frotamotors.infrastructure.dto.LoginRequestDTO;
import com.example.frotamotors.infrastructure.dto.RefreshTokenRequestDTO;
import com.example.frotamotors.infrastructure.dto.ResetPasswordRequestDTO;
import com.example.frotamotors.infrastructure.dto.UserMeResponseDTO;
import com.example.frotamotors.infrastructure.security.CustomUserDetailsService.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  @Autowired private AuthService authService;

  @Autowired private UserService userService;

  // OAuth2 endpoints commented out - OAuth2 is not configured
  // @PostMapping("/google")
  // public ResponseEntity<AuthResponseDTO> authenticateWithGoogle(
  //     @Valid @RequestBody GoogleAuthRequestDTO request) {
  //   AuthResponseDTO response = authService.authenticateOrCreateGoogleUser(request.idToken());
  //   return ResponseEntity.ok(response);
  // }

  // @PostMapping("/apple")
  // public ResponseEntity<AuthResponseDTO> authenticateWithApple(
  //     @Valid @RequestBody AppleAuthRequestDTO request) {
  //   AuthResponseDTO response = authService.authenticateOrCreateAppleUser(request.idToken());
  //   return ResponseEntity.ok(response);
  // }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
    AuthResponseDTO response = authService.authenticateLocalUser(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthResponseDTO> refresh(
      @Valid @RequestBody RefreshTokenRequestDTO request) {
    AuthResponseDTO response = authService.refreshToken(request.refreshToken());
    return ResponseEntity.ok(response);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
      CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
      authService.logout(userDetails.getUserId());
    }
    return ResponseEntity.ok().build();
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO request) {
    authService.requestPasswordReset(request);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/reset-password")
  public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {
    authService.resetPassword(request);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/me")
  public ResponseEntity<UserMeResponseDTO> getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
      CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
      User user = userService.getUserById(userDetails.getUserId());
      if (user == null) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
      }

      // Ensure permissions are set
      if (user.getPermissions() == null || user.getPermissions().isEmpty()) {
        // This should not happen, but handle it gracefully
        user.setPermissions(authService.getDefaultPermissions(user.getRole()));
        user = userService.updateUser(user);
      }

      UserMeResponseDTO response =
          new UserMeResponseDTO(
              user.getId(),
              user.getName(),
              user.getEmail(),
              user.getRole(),
              user.getPermissions(),
              user.getImageUrl(),
              user.getLastLogin());
      return ResponseEntity.ok(response);
    }
    return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
  }
}
