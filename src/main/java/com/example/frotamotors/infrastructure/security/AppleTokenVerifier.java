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
import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;

// @Component - Commented out to prevent auto-instantiation when OAuth2 is not configured
public class AppleTokenVerifier {

  private static final String APPLE_ISSUER = "https://appleid.apple.com";
  private static final String APPLE_JWKS_URL = "https://appleid.apple.com/auth/keys";

  @Value("${spring.security.oauth2.client.registration.apple.client-id:}")
  private String clientId;

  public JWTClaimsSet verifyToken(String idTokenString) throws Exception {
    if (clientId == null || clientId.isEmpty()) {
      throw new IllegalStateException("Apple Client ID not configured");
    }

    try {
      // Fetch Apple's public keys
      JWKSet jwkSet = JWKSet.load(new URL(APPLE_JWKS_URL));

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
        throw new SecurityException("Invalid token issuer: " + issuer);
      }

      // Verify audience (client ID)
      String audience = claimsSet.getAudience().iterator().next();
      if (!audience.equals(clientId)) {
        throw new SecurityException("Invalid token audience: " + audience);
      }

      // Verify expiration
      if (claimsSet.getExpirationTime() != null
          && claimsSet.getExpirationTime().before(new java.util.Date())) {
        throw new SecurityException("Token has expired");
      }

      return claimsSet;
    } catch (ParseException | JOSEException | BadJOSEException e) {
      throw new SecurityException("Failed to verify Apple token: " + e.getMessage(), e);
    }
  }
}

