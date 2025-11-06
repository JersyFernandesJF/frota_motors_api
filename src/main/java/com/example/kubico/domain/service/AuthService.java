package com.example.kubico.domain.service;

import com.example.kubico.domain.enums.Role;
import com.example.kubico.domain.model.PasswordResetToken;
import com.example.kubico.domain.model.RefreshToken;
import com.example.kubico.domain.model.User;
import com.example.kubico.infrastructure.dto.AuthResponseDTO;
import com.example.kubico.infrastructure.dto.ForgotPasswordRequestDTO;
import com.example.kubico.infrastructure.dto.LoginRequestDTO;
import com.example.kubico.infrastructure.dto.ResetPasswordRequestDTO;
import com.example.kubico.infrastructure.persistence.PasswordResetTokenRepository;
import com.example.kubico.infrastructure.persistence.RefreshTokenRepository;
import com.example.kubico.infrastructure.persistence.UserRepository;
import com.example.kubico.infrastructure.security.AppleTokenVerifier;
import com.example.kubico.infrastructure.security.GoogleTokenVerifier;
import com.example.kubico.infrastructure.security.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.nimbusds.jwt.JWTClaimsSet;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AuthService {

  @Autowired private UserRepository userRepository;

  @Autowired private JwtTokenProvider jwtTokenProvider;

  @Autowired private PasswordEncoder passwordEncoder;

  @Autowired private RefreshTokenRepository refreshTokenRepository;

  @Autowired private PasswordResetTokenRepository passwordResetTokenRepository;

  @Autowired private GoogleTokenVerifier googleTokenVerifier;

  @Autowired private AppleTokenVerifier appleTokenVerifier;

  @Value("${jwt.refresh-expiration:604800000}") // 7 days default
  private long refreshTokenExpiration;

  @Value("${jwt.reset-token-expiration:3600000}") // 1 hour default
  private long resetTokenExpiration;

  @Transactional
  public AuthResponseDTO authenticateOrCreateGoogleUser(String idToken) {
    try {
      // Verify Google ID token
      GoogleIdToken.Payload payload = googleTokenVerifier.verifyToken(idToken);
      
      String googleId = payload.getSubject();
      String email = payload.getEmail();
      String name = (String) payload.get("name");
      String imageUrl = (String) payload.get("picture");
      
      log.info("Google authentication successful for user: {}", email);

      Optional<User> existingUser = userRepository.findByGoogleId(googleId);

      User user;
      if (existingUser.isPresent()) {
        user = existingUser.get();
        // Update user info if needed
        if (name != null && !name.equals(user.getName())) {
          user.setName(name);
        }
        if (imageUrl != null && !imageUrl.equals(user.getImageUrl())) {
          user.setImageUrl(imageUrl);
        }
      } else {
        // Check if user exists with this email
        Optional<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isPresent()) {
          user = userByEmail.get();
          user.setGoogleId(googleId);
          user.setProvider("GOOGLE");
          if (imageUrl != null) {
            user.setImageUrl(imageUrl);
          }
        } else {
          // Create new user
          user = new User();
          user.setName(name != null ? name : email);
          user.setEmail(email);
          user.setGoogleId(googleId);
          user.setProvider("GOOGLE");
          user.setRole(Role.BUYER);
          user.setImageUrl(imageUrl);
        }
      }

      user = userRepository.save(user);
      String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());
      String refreshToken = createRefreshToken(user);

      return new AuthResponseDTO(token, refreshToken, user.getId(), user.getEmail(), user.getName(), user.getRole());
    } catch (Exception e) {
      log.error("Google authentication failed: {}", e.getMessage());
      throw new BadCredentialsException("Invalid Google token: " + e.getMessage());
    }
  }

  @Transactional
  public AuthResponseDTO authenticateOrCreateAppleUser(String idToken) {
    try {
      // Verify Apple ID token
      JWTClaimsSet claimsSet = appleTokenVerifier.verifyToken(idToken);
      
      String appleId = claimsSet.getSubject();
      String email = (String) claimsSet.getClaim("email");
      String name = (String) claimsSet.getClaim("name");
      
      log.info("Apple authentication successful for user: {}", email);

      Optional<User> existingUser = userRepository.findByAppleId(appleId);

      User user;
      if (existingUser.isPresent()) {
        user = existingUser.get();
        if (name != null && !name.equals(user.getName())) {
          user.setName(name);
        }
      } else {
        // Check if user exists with this email
        Optional<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isPresent()) {
          user = userByEmail.get();
          user.setAppleId(appleId);
          user.setProvider("APPLE");
        } else {
          // Create new user
          user = new User();
          user.setName(name != null ? name : email);
          user.setEmail(email);
          user.setAppleId(appleId);
          user.setProvider("APPLE");
          user.setRole(Role.BUYER);
        }
      }

      user = userRepository.save(user);
      String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());
      String refreshToken = createRefreshToken(user);

      return new AuthResponseDTO(token, refreshToken, user.getId(), user.getEmail(), user.getName(), user.getRole());
    } catch (Exception e) {
      log.error("Apple authentication failed: {}", e.getMessage());
      throw new BadCredentialsException("Invalid Apple token: " + e.getMessage());
    }
  }

  public AuthResponseDTO authenticateLocalUser(LoginRequestDTO request) {
    try {
      User user =
          userRepository
              .findByEmail(request.email())
              .orElseThrow(() -> {
                log.warn("Login attempt with non-existent email: {}", request.email());
                return new BadCredentialsException("Invalid email or password");
              });

      // Check if user has a password (OAuth users might not have one)
      if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty()) {
        log.warn("Login attempt for OAuth user without password: {}", request.email());
        throw new BadCredentialsException("Invalid email or password");
      }

      // Verify password
      if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
        log.warn("Failed login attempt for user: {}", request.email());
        throw new BadCredentialsException("Invalid email or password");
      }

      log.info("Successful login for user: {}", request.email());
      String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());
      String refreshToken = createRefreshToken(user);

      return new AuthResponseDTO(token, refreshToken, user.getId(), user.getEmail(), user.getName(), user.getRole());
    } catch (BadCredentialsException e) {
      throw e;
    } catch (Exception e) {
      log.error("Login error for user: {}", request.email(), e);
      throw new BadCredentialsException("Invalid email or password");
    }
  }

  @Transactional
  public String createRefreshToken(User user) {
    // Revoke existing tokens
    refreshTokenRepository.revokeAllUserTokens(user.getId());

    // Create new refresh token
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUser(user);
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken.setExpiresAt(LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000));
    refreshToken.setRevoked(false);

    RefreshToken saved = refreshTokenRepository.save(refreshToken);
    return saved.getToken();
  }

  @Transactional
  public AuthResponseDTO refreshToken(String refreshTokenString) {
    RefreshToken refreshToken =
        refreshTokenRepository
            .findByTokenAndRevokedFalse(refreshTokenString)
            .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

    if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new BadCredentialsException("Refresh token has expired");
    }

    User user = refreshToken.getUser();
    String newAccessToken = jwtTokenProvider.generateToken(user.getId(), user.getEmail());
    String newRefreshToken = createRefreshToken(user);

    return new AuthResponseDTO(
        newAccessToken, newRefreshToken, user.getId(), user.getEmail(), user.getName(), user.getRole());
  }

  @Transactional
  public void logout(UUID userId) {
    refreshTokenRepository.revokeAllUserTokens(userId);
  }

  @Transactional
  public void requestPasswordReset(ForgotPasswordRequestDTO request) {
    try {
      User user =
          userRepository
              .findByEmail(request.email())
              .orElseThrow(() -> {
                // Don't reveal if email exists for security
                log.warn("Password reset requested for non-existent email: {}", request.email());
                return new jakarta.persistence.EntityNotFoundException("User not found");
              });

      log.info("Password reset requested for user: {}", request.email());

      // Invalidate existing tokens
      passwordResetTokenRepository.invalidateAllUserTokens(user.getId());

      // Create new reset token
      PasswordResetToken resetToken = new PasswordResetToken();
      resetToken.setUser(user);
      resetToken.setToken(UUID.randomUUID().toString());
      resetToken.setExpiresAt(LocalDateTime.now().plusSeconds(resetTokenExpiration / 1000));
      resetToken.setUsed(false);

      passwordResetTokenRepository.save(resetToken);

      // TODO: Send email with reset link
      // For now, the token is returned in the response (for development)
      // In production, send email with link containing the token
      log.info("Password reset token created for user: {}", request.email());
    } catch (jakarta.persistence.EntityNotFoundException e) {
      // Don't reveal if email exists
      log.warn("Password reset attempt for non-existent email");
      throw e;
    }
  }

  @Transactional
  public void resetPassword(ResetPasswordRequestDTO request) {
    PasswordResetToken resetToken =
        passwordResetTokenRepository
            .findByTokenAndUsedFalse(request.token())
            .orElseThrow(() -> {
              log.warn("Password reset attempt with invalid token");
              return new BadCredentialsException("Invalid or expired reset token");
            });

    if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
      log.warn("Password reset attempt with expired token");
      throw new BadCredentialsException("Reset token has expired");
    }

    if (resetToken.getUsed()) {
      log.warn("Password reset attempt with already used token");
      throw new BadCredentialsException("Reset token has already been used");
    }

    User user = resetToken.getUser();
    log.info("Password reset successful for user: {}", user.getEmail());
    
    String encodedPassword = passwordEncoder.encode(request.newPassword());
    user.setPasswordHash(encodedPassword);
    userRepository.save(user);

    // Mark token as used
    resetToken.setUsed(true);
    passwordResetTokenRepository.save(resetToken);

    // Revoke all refresh tokens for security
    refreshTokenRepository.revokeAllUserTokens(user.getId());
  }
}

