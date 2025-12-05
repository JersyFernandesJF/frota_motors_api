package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.enums.Role;
import com.example.frotamotors.domain.model.PasswordResetToken;
import com.example.frotamotors.domain.model.RefreshToken;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.infrastructure.dto.AuthResponseDTO;
import com.example.frotamotors.infrastructure.dto.ForgotPasswordRequestDTO;
import com.example.frotamotors.infrastructure.dto.LoginRequestDTO;
import com.example.frotamotors.infrastructure.dto.ResetPasswordRequestDTO;
import com.example.frotamotors.infrastructure.persistence.PasswordResetTokenRepository;
import com.example.frotamotors.infrastructure.persistence.RefreshTokenRepository;
import com.example.frotamotors.infrastructure.persistence.UserRepository;
import com.example.frotamotors.infrastructure.security.AppleTokenVerifier;
import com.example.frotamotors.infrastructure.security.GoogleTokenVerifier;
import com.example.frotamotors.infrastructure.security.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.nimbusds.jwt.JWTClaimsSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired(required = false)
	private GoogleTokenVerifier googleTokenVerifier;

	@Autowired(required = false)
	private AppleTokenVerifier appleTokenVerifier;

	@Value("${jwt.refresh-expiration:604800000}") // 7 days default
	private long refreshTokenExpiration;

	@Value("${jwt.reset-token-expiration:3600000}") // 1 hour default
	private long resetTokenExpiration;

<<<<<<< Updated upstream
	@Transactional
	public AuthResponseDTO authenticateOrCreateGoogleUser(String idToken) {
		if (googleTokenVerifier == null) {
			throw new IllegalStateException("Google authentication is not configured");
		}
		try {
			// Verify Google ID token
			GoogleIdToken.Payload payload = googleTokenVerifier.verifyToken(idToken);
=======
  @Transactional
  public AuthResponseDTO authenticateOrCreateGoogleUser(String idToken) {
    if (googleTokenVerifier == null) {
      throw new IllegalStateException("Google authentication is not configured");
    }
    if (idToken == null || idToken.isBlank()) {
      log.warn("Google authentication attempted with empty token");
      throw new BadCredentialsException("Google ID token is required");
    }

    try {
      // Verify Google ID token - this is the only trusted source of user data
      GoogleIdToken.Payload payload = googleTokenVerifier.verifyToken(idToken);
>>>>>>> Stashed changes

			String googleId = payload.getSubject();
			String email = payload.getEmail();
			String name = (String) payload.get("name");
			String imageUrl = (String) payload.get("picture");

<<<<<<< Updated upstream
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
=======
      if (email == null || email.isBlank()) {
        log.warn("Google token missing email claim");
        throw new BadCredentialsException("Google token does not contain email");
      }

      log.info("Google authentication successful for user: {} (Google ID: {})", email, googleId);

      // First, check if user exists with this Google ID
      Optional<User> existingUser = userRepository.findByGoogleId(googleId);

      User user;
      if (existingUser.isPresent()) {
        // User already linked with this Google account
        user = existingUser.get();
        log.debug("Found existing user with Google ID: {}", googleId);

        // Update user info if needed (name, image can change)
        boolean updated = false;
        if (name != null && !name.equals(user.getName())) {
          user.setName(name);
          updated = true;
        }
        if (imageUrl != null && !imageUrl.equals(user.getImageUrl())) {
          user.setImageUrl(imageUrl);
          updated = true;
        }
        if (updated) {
          log.debug("Updated user info for Google account: {}", email);
        }
      } else {
        // Check if user exists with this email (account linking)
        Optional<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isPresent()) {
          user = userByEmail.get();
          log.info("Linking existing user {} with Google account", email);

          // Check if email is already linked to another provider
          if (user.getGoogleId() != null && !user.getGoogleId().equals(googleId)) {
            log.warn(
                "Email {} already linked to different Google account. Updating link.",
                email);
          }
          if (user.getAppleId() != null) {
            log.info("User {} has both Google and Apple accounts linked", email);
          }

          // Link Google account
          user.setGoogleId(googleId);
          if (user.getProvider() == null || user.getProvider().isBlank()) {
            user.setProvider("GOOGLE");
          } else if (!user.getProvider().contains("GOOGLE")) {
            // User has multiple providers
            user.setProvider(user.getProvider() + ",GOOGLE");
          }
          if (imageUrl != null && (user.getImageUrl() == null || user.getImageUrl().isBlank())) {
            user.setImageUrl(imageUrl);
          }
        } else {
          // Create new user
          log.info("Creating new user with Google account: {}", email);
          user = new User();
          user.setName(name != null && !name.isBlank() ? name : email);
          user.setEmail(email);
          user.setGoogleId(googleId);
          user.setProvider("GOOGLE");
          user.setRole(Role.BUYER);
          user.setImageUrl(imageUrl);
        }
      }
>>>>>>> Stashed changes

			// Update last login
			user.setLastLogin(LocalDateTime.now());
			user = userRepository.save(user);

			// Ensure permissions are set
			if (user.getPermissions() == null || user.getPermissions().isEmpty()) {
				user.setPermissions(getDefaultPermissions(user.getRole()));
				user = userRepository.save(user);
			}

			String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());
			String refreshToken = createRefreshToken(user);

<<<<<<< Updated upstream
			return new AuthResponseDTO(
					token,
					refreshToken,
					user.getId(),
					user.getEmail(),
					user.getName(),
					user.getRole(),
					user.getPermissions());
		} catch (Exception e) {
			log.error("Google authentication failed: {}", e.getMessage());
			throw new BadCredentialsException("Invalid Google token: " + e.getMessage());
		}
	}

	@Transactional
	public AuthResponseDTO authenticateOrCreateAppleUser(String idToken) {
		if (appleTokenVerifier == null) {
			throw new IllegalStateException("Apple authentication is not configured");
		}
		try {
			// Verify Apple ID token
			JWTClaimsSet claimsSet = appleTokenVerifier.verifyToken(idToken);
=======
      log.info("Google authentication completed successfully for user: {}", user.getEmail());
      return new AuthResponseDTO(
          token,
          refreshToken,
          user.getId(),
          user.getEmail(),
          user.getName(),
          user.getRole(),
          user.getPermissions());
    } catch (IllegalStateException | SecurityException e) {
      // Re-throw configuration and security exceptions as-is
      throw e;
    } catch (BadCredentialsException e) {
      // Re-throw authentication exceptions as-is
      throw e;
    } catch (Exception e) {
      log.error("Google authentication failed: {}", e.getMessage(), e);
      throw new BadCredentialsException("Invalid Google token: " + e.getMessage());
    }
  }

  @Transactional
  public AuthResponseDTO authenticateOrCreateAppleUser(String idToken) {
    if (appleTokenVerifier == null) {
      throw new IllegalStateException("Apple authentication is not configured");
    }
    if (idToken == null || idToken.isBlank()) {
      log.warn("Apple authentication attempted with empty token");
      throw new BadCredentialsException("Apple ID token is required");
    }

    try {
      // Verify Apple ID token - this is the only trusted source of user data
      JWTClaimsSet claimsSet = appleTokenVerifier.verifyToken(idToken);
>>>>>>> Stashed changes

			String appleId = claimsSet.getSubject();
			String email = (String) claimsSet.getClaim("email");
			String name = (String) claimsSet.getClaim("name");

<<<<<<< Updated upstream
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
=======
      // Note: Apple may not always provide email in the token (privacy feature)
      // If email is not in token, we need to handle it differently
      if (appleId == null || appleId.isBlank()) {
        log.warn("Apple token missing subject claim");
        throw new BadCredentialsException("Apple token does not contain user ID");
      }

      log.info(
          "Apple authentication successful for user: {} (Apple ID: {})",
          email != null ? email : "email not provided",
          appleId);

      // First, check if user exists with this Apple ID
      Optional<User> existingUser = userRepository.findByAppleId(appleId);

      User user;
      if (existingUser.isPresent()) {
        // User already linked with this Apple account
        user = existingUser.get();
        log.debug("Found existing user with Apple ID: {}", appleId);

        // Update user info if needed (name can change)
        if (name != null && !name.equals(user.getName())) {
          user.setName(name);
        }
        // Update email if it was not provided before but is now available
        if (email != null && !email.isBlank() && (user.getEmail() == null || user.getEmail().isBlank())) {
          log.info("Updating email for Apple user: {}", appleId);
          user.setEmail(email);
        }
      } else {
        // Check if user exists with this email (account linking)
        if (email != null && !email.isBlank()) {
          Optional<User> userByEmail = userRepository.findByEmail(email);
          if (userByEmail.isPresent()) {
            user = userByEmail.get();
            log.info("Linking existing user {} with Apple account", email);

            // Check if email is already linked to another provider
            if (user.getAppleId() != null && !user.getAppleId().equals(appleId)) {
              log.warn(
                  "Email {} already linked to different Apple account. Updating link.",
                  email);
            }
            if (user.getGoogleId() != null) {
              log.info("User {} has both Google and Apple accounts linked", email);
            }

            // Link Apple account
            user.setAppleId(appleId);
            if (user.getProvider() == null || user.getProvider().isBlank()) {
              user.setProvider("APPLE");
            } else if (!user.getProvider().contains("APPLE")) {
              // User has multiple providers
              user.setProvider(user.getProvider() + ",APPLE");
            }
          } else {
            // Create new user with email
            log.info("Creating new user with Apple account: {}", email);
            user = new User();
            user.setName(name != null && !name.isBlank() ? name : email);
            user.setEmail(email);
            user.setAppleId(appleId);
            user.setProvider("APPLE");
            user.setRole(Role.BUYER);
          }
        } else {
          // Apple privacy: email not provided, create user with Apple ID only
          // Note: This is a limitation - we need email for the system
          // In production, you might want to prompt user for email after first login
          log.warn("Apple token does not contain email. Creating user with Apple ID only.");
          throw new BadCredentialsException(
              "Apple token does not contain email. Email is required for account creation.");
        }
      }
>>>>>>> Stashed changes

			// Update last login
			user.setLastLogin(LocalDateTime.now());
			user = userRepository.save(user);

			// Ensure permissions are set
			if (user.getPermissions() == null || user.getPermissions().isEmpty()) {
				user.setPermissions(getDefaultPermissions(user.getRole()));
				user = userRepository.save(user);
			}

			String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());
			String refreshToken = createRefreshToken(user);

<<<<<<< Updated upstream
			return new AuthResponseDTO(
					token,
					refreshToken,
					user.getId(),
					user.getEmail(),
					user.getName(),
					user.getRole(),
					user.getPermissions());
		} catch (Exception e) {
			log.error("Apple authentication failed: {}", e.getMessage());
			throw new BadCredentialsException("Invalid Apple token: " + e.getMessage());
		}
	}
=======
      log.info("Apple authentication completed successfully for user: {}", user.getEmail());
      return new AuthResponseDTO(
          token,
          refreshToken,
          user.getId(),
          user.getEmail(),
          user.getName(),
          user.getRole(),
          user.getPermissions());
    } catch (IllegalStateException | SecurityException e) {
      // Re-throw configuration and security exceptions as-is
      throw e;
    } catch (BadCredentialsException e) {
      // Re-throw authentication exceptions as-is
      throw e;
    } catch (Exception e) {
      log.error("Apple authentication failed: {}", e.getMessage(), e);
      throw new BadCredentialsException("Invalid Apple token: " + e.getMessage());
    }
  }
>>>>>>> Stashed changes

	@Transactional
	public AuthResponseDTO authenticateLocalUser(LoginRequestDTO request) {
		try {
			User user = userRepository
					.findByEmail(request.email())
					.orElseThrow(
							() -> {
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

			// Update last login
			user.setLastLogin(LocalDateTime.now());
			user = userRepository.save(user);

			// Ensure permissions are set
			if (user.getPermissions() == null || user.getPermissions().isEmpty()) {
				user.setPermissions(getDefaultPermissions(user.getRole()));
				user = userRepository.save(user);
			}

			String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());
			String refreshToken = createRefreshToken(user);

			return new AuthResponseDTO(
					token,
					refreshToken,
					user.getId(),
					user.getEmail(),
					user.getName(),
					user.getRole(),
					user.getPermissions());
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
		RefreshToken refreshToken = refreshTokenRepository
				.findByTokenAndRevokedFalse(refreshTokenString)
				.orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

		if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
			throw new BadCredentialsException("Refresh token has expired");
		}

		User user = refreshToken.getUser();

		// Ensure permissions are set
		if (user.getPermissions() == null || user.getPermissions().isEmpty()) {
			user.setPermissions(getDefaultPermissions(user.getRole()));
			user = userRepository.save(user);
		}

		String newAccessToken = jwtTokenProvider.generateToken(user.getId(), user.getEmail());
		String newRefreshToken = createRefreshToken(user);

		return new AuthResponseDTO(
				newAccessToken,
				newRefreshToken,
				user.getId(),
				user.getEmail(),
				user.getName(),
				user.getRole(),
				user.getPermissions());
	}

	@Transactional
	public void logout(UUID userId) {
		refreshTokenRepository.revokeAllUserTokens(userId);
	}

	@Transactional
	public void requestPasswordReset(ForgotPasswordRequestDTO request) {
		try {
			User user = userRepository
					.findByEmail(request.email())
					.orElseThrow(
							() -> {
								// Don't reveal if email exists for security
								log.warn(
										"Password reset requested for non-existent email: {}", request.email());
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
		PasswordResetToken resetToken = passwordResetTokenRepository
				.findByTokenAndUsedFalse(request.token())
				.orElseThrow(
						() -> {
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

	public List<String> getDefaultPermissions(Role role) {
		List<String> permissions = new ArrayList<>();
		switch (role) {
			case ADMIN:
				permissions.add("*"); // All permissions
				break;
			case MODERATOR:
				permissions.add("users.read");
				permissions.add("users.update");
				permissions.add("listings.read");
				permissions.add("listings.approve");
				permissions.add("listings.reject");
				permissions.add("reports.read");
				permissions.add("reports.resolve");
				permissions.add("messages.read");
				permissions.add("messages.delete");
				break;
			case AGENT:
			case OWNER:
				permissions.add("listings.create");
				permissions.add("listings.update");
				permissions.add("listings.delete");
				permissions.add("messages.read");
				permissions.add("messages.send");
				break;
			case BUYER:
			default:
				permissions.add("listings.read");
				permissions.add("messages.read");
				permissions.add("messages.send");
				break;
		}
		return permissions;
	}
}
