package com.example.frotamotors.infrastructure.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;

// @Component - Commented out to prevent auto-instantiation when OAuth2 is not configured
public class GoogleTokenVerifier {

  @Value("${spring.security.oauth2.client.registration.google.client-id:}")
  private String clientId;

  public GoogleIdToken.Payload verifyToken(String idTokenString) throws Exception {
    if (clientId == null || clientId.isEmpty()) {
      throw new IllegalStateException("Google Client ID not configured");
    }

    GoogleIdTokenVerifier verifier =
        new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
            .setAudience(Collections.singletonList(clientId))
            .build();

    GoogleIdToken idToken = verifier.verify(idTokenString);
    if (idToken == null) {
      throw new SecurityException("Invalid Google ID token");
    }

    GoogleIdToken.Payload payload = idToken.getPayload();
    
    // Verify issuer
    String issuer = payload.getIssuer();
    if (!issuer.equals("https://accounts.google.com") && !issuer.equals("accounts.google.com")) {
      throw new SecurityException("Invalid token issuer: " + issuer);
    }

    return payload;
  }
}

