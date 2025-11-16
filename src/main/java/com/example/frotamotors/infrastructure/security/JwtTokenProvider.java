package com.example.frotamotors.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  @Value("${jwt.secret:}")
  private String jwtSecret;

  @Value("${jwt.expiration:86400000}") // 24 hours
  private long jwtExpiration;

  private SecretKey getSigningKey() {
    // Get secret from environment variable first, then from property
    String secret = System.getenv("JWT_SECRET");
    if (secret == null || secret.isEmpty()) {
      secret = jwtSecret;
    }

    if (secret == null || secret.isEmpty()) {
      throw new IllegalStateException(
          "JWT secret must be configured via JWT_SECRET environment variable or jwt.secret property");
    }

    // Validate minimum key length (256 bits = 32 bytes)
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    if (keyBytes.length < 32) {
      throw new IllegalStateException(
          "JWT secret must be at least 256 bits (32 bytes) long for security");
    }

    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateToken(UUID userId, String email) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpiration);

    return Jwts.builder()
        .subject(userId.toString())
        .claim("email", email)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey())
        .compact();
  }

  public UUID getUserIdFromToken(String token) {
    String subject = getClaimFromToken(token, Claims::getSubject);
    return UUID.fromString(subject);
  }

  public String getEmailFromToken(String token) {
    return getClaimFromToken(token, claims -> claims.get("email", String.class));
  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
  }

  public Boolean validateToken(String token) {
    try {
      Claims claims = getAllClaimsFromToken(token);
      return !claims.getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }
}
