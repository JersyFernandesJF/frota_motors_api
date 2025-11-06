package com.example.kubico.infrastructure.web;

import com.example.kubico.domain.service.AuthService;
import com.example.kubico.infrastructure.dto.AppleAuthRequestDTO;
import com.example.kubico.infrastructure.dto.AuthResponseDTO;
import com.example.kubico.infrastructure.dto.ForgotPasswordRequestDTO;
import com.example.kubico.infrastructure.dto.GoogleAuthRequestDTO;
import com.example.kubico.infrastructure.dto.LoginRequestDTO;
import com.example.kubico.infrastructure.dto.RefreshTokenRequestDTO;
import com.example.kubico.infrastructure.dto.ResetPasswordRequestDTO;
import com.example.kubico.infrastructure.security.CustomUserDetailsService.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  @Autowired private AuthService authService;

  @PostMapping("/google")
  public ResponseEntity<AuthResponseDTO> authenticateWithGoogle(
      @Valid @RequestBody GoogleAuthRequestDTO request) {
    AuthResponseDTO response = authService.authenticateOrCreateGoogleUser(request.idToken());
    return ResponseEntity.ok(response);
  }

  @PostMapping("/apple")
  public ResponseEntity<AuthResponseDTO> authenticateWithApple(
      @Valid @RequestBody AppleAuthRequestDTO request) {
    AuthResponseDTO response = authService.authenticateOrCreateAppleUser(request.idToken());
    return ResponseEntity.ok(response);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
    AuthResponseDTO response = authService.authenticateLocalUser(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO request) {
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
  public ResponseEntity<Void> forgotPassword(
      @Valid @RequestBody ForgotPasswordRequestDTO request) {
    authService.requestPasswordReset(request);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/reset-password")
  public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {
    authService.resetPassword(request);
    return ResponseEntity.ok().build();
  }
}

