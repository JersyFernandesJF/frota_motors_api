package com.example.frotamotors.infrastructure.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "oauth.apple.enabled", havingValue = "true", matchIfMissing = false)
public class AppleTokenVerifier {

  private static final String APPLE_ISSUER = "https://appleid.apple.com";
  private static final String APPLE_JWKS_URL = "https://appleid.apple.com/auth/keys";
  private static final long JWKS_CACHE_TTL_SECONDS = 3600; // 1 hour

  @Value("${oauth.apple.client-id:}")
  private String clientId;

  private JWKSet cachedJwkSet;
  private Instant cacheExpiry;
  private final ReentrantLock cacheLock = new ReentrantLock();

  public JWTClaimsSet verifyToken(String idTokenString) throws Exception {
    if (clientId == null || clientId.isEmpty()) {
      log.error("Apple Client ID not configured");
      throw new IllegalStateException("Apple Client ID not configured");
    }

    try {
      // Get JWKS from cache or fetch new
      JWKSet jwkSet = getJwkSet();

      // Configure JWT processor
      ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
      JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(jwkSet);
      JWSKeySelector<SecurityContext> keySelector =
          new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, jwkSource);
      jwtProcessor.setJWSKeySelector(keySelector);

      // Process and verify token
      JWTClaimsSet claimsSet = jwtProcessor.process(idTokenString, null);

      // Verify issuer
      String issuer = claimsSet.getIssuer();
      if (!issuer.equals(APPLE_ISSUER)) {
        log.warn("Invalid Apple token issuer: {}", issuer);
        throw new SecurityException("Invalid token issuer: " + issuer);
      }

      // Verify audience (client ID)
      String audience = claimsSet.getAudience().iterator().next();
      if (!audience.equals(clientId)) {
        log.warn("Invalid Apple token audience: {}", audience);
        throw new SecurityException("Invalid token audience: " + audience);
      }

      // Verify expiration
      if (claimsSet.getExpirationTime() != null
          && claimsSet.getExpirationTime().before(new java.util.Date())) {
        log.warn("Apple token has expired");
        throw new SecurityException("Token has expired");
      }

      log.debug("Apple token verified successfully for subject: {}", claimsSet.getSubject());
      return claimsSet;
    } catch (SecurityException e) {
      throw e;
    } catch (ParseException | JOSEException | BadJOSEException e) {
      log.error("Error verifying Apple token", e);
      throw new SecurityException("Failed to verify Apple token: " + e.getMessage(), e);
    }
  }

  private JWKSet getJwkSet() throws Exception {
    cacheLock.lock();
    try {
      // Check if cache is valid
      if (cachedJwkSet != null && cacheExpiry != null && Instant.now().isBefore(cacheExpiry)) {
        log.debug("Using cached Apple JWKS");
        return cachedJwkSet;
      }

      // Fetch new JWKS
      log.info("Fetching Apple JWKS from {}", APPLE_JWKS_URL);
      cachedJwkSet = JWKSet.load(new URL(APPLE_JWKS_URL));
      cacheExpiry = Instant.now().plusSeconds(JWKS_CACHE_TTL_SECONDS);
      return cachedJwkSet;
    } finally {
      cacheLock.unlock();
    }
  }
}
