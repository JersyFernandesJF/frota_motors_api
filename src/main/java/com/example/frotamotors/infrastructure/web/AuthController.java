package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.service.AuthService;
import com.example.frotamotors.domain.service.UserService;
import com.example.frotamotors.infrastructure.dto.AppleAuthRequestDTO;
import com.example.frotamotors.infrastructure.dto.AuthResponseDTO;
import com.example.frotamotors.infrastructure.dto.ErrorResponseDTO;
import com.example.frotamotors.infrastructure.dto.ForgotPasswordRequestDTO;
import com.example.frotamotors.infrastructure.dto.GoogleAuthRequestDTO;
import com.example.frotamotors.infrastructure.dto.LoginRequestDTO;
import com.example.frotamotors.infrastructure.dto.RefreshTokenRequestDTO;
import com.example.frotamotors.infrastructure.dto.ResetPasswordRequestDTO;
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

  @PostMapping("/google")
  @io.swagger.v3.oas.annotations.Operation(
      summary = "Authenticate with Google",
      description =
          "Authenticate or register a user using Google Sign-In. Requires a valid Google ID token.")
  @io.swagger.v3.oas.annotations.responses.ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Successfully authenticated with Google"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid Google token or authentication failed"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "503",
            description = "Google authentication is not configured")
      })
  public ResponseEntity<?> authenticateWithGoogle(
      @Valid @RequestBody GoogleAuthRequestDTO request) {
    try {
      AuthResponseDTO response = authService.authenticateOrCreateGoogleUser(request.idToken());
      log.info("Google authentication successful");
      return ResponseEntity.ok(response);
    } catch (IllegalStateException e) {
      log.warn("Google authentication not configured: {}", e.getMessage());
      return ResponseEntity.status(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE)
          .body(
              new ErrorResponseDTO(
                  "Google authentication is not configured", "GOOGLE_NOT_CONFIGURED"));
    } catch (org.springframework.security.authentication.BadCredentialsException e) {
      log.warn("Google authentication failed: {}", e.getMessage());
      return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST)
          .body(new ErrorResponseDTO("Invalid Google token", "INVALID_GOOGLE_TOKEN"));
    } catch (Exception e) {
      log.error("Unexpected error during Google authentication", e);
      return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new ErrorResponseDTO(
                  "Authentication failed. Please try again later.", "GOOGLE_AUTH_ERROR"));
    }
  }

  @PostMapping("/apple")
  @io.swagger.v3.oas.annotations.Operation(
      summary = "Authenticate with Apple",
      description =
          "Authenticate or register a user using Sign in with Apple. Requires a valid Apple ID token.")
  @io.swagger.v3.oas.annotations.responses.ApiResponses(
      value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Successfully authenticated with Apple"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid Apple token or authentication failed"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "503",
            description = "Apple authentication is not configured")
      })
  public ResponseEntity<?> authenticateWithApple(@Valid @RequestBody AppleAuthRequestDTO request) {
    try {
      AuthResponseDTO response = authService.authenticateOrCreateAppleUser(request.idToken());
      log.info("Apple authentication successful");
      return ResponseEntity.ok(response);
    } catch (IllegalStateException e) {
      log.warn("Apple authentication not configured: {}", e.getMessage());
      return ResponseEntity.status(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE)
          .body(
              new ErrorResponseDTO(
                  "Apple authentication is not configured", "APPLE_NOT_CONFIGURED"));
    } catch (org.springframework.security.authentication.BadCredentialsException e) {
      log.warn("Apple authentication failed: {}", e.getMessage());
      return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST)
          .body(new ErrorResponseDTO("Invalid Apple token", "INVALID_APPLE_TOKEN"));
    } catch (Exception e) {
      log.error("Unexpected error during Apple authentication", e);
      return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new ErrorResponseDTO(
                  "Authentication failed. Please try again later.", "APPLE_AUTH_ERROR"));
    }
  }

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
  public ResponseEntity<AuthResponseDTO> getCurrentUser() {
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

      // Return AuthResponseDTO format for compatibility with frontend
      // Note: token and refreshToken are not included as they should be in the request header
      // Frontend should use the token from localStorage
      AuthResponseDTO response =
          new AuthResponseDTO(
              null, // token - not returned for security reasons, use from header
              null, // refreshToken - not returned for security reasons
              user.getId(),
              user.getEmail(),
              user.getName(),
              user.getRole(),
              user.getPermissions());
      return ResponseEntity.ok(response);
    }
    return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
  }
}
