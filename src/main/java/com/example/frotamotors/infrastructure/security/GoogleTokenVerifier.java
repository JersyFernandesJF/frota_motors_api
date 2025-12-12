package com.example.frotamotors.infrastructure.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.util.Collections;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(name = "oauth.google.enabled", havingValue = "true", matchIfMissing = false)
public class GoogleTokenVerifier {

	private static final String GOOGLE_ISSUER_1 = "https://accounts.google.com";
	private static final String GOOGLE_ISSUER_2 = "accounts.google.com";

	@Value("${oauth.google.client-id:}")
	private String clientId;

	@PostConstruct
	public void init() {
		log.info("GoogleTokenVerifier initialized with clientId: {}",
				clientId != null && !clientId.isEmpty() ? clientId.substring(0, Math.min(20, clientId.length())) + "..."
						: "NOT SET");
	}

	public GoogleIdToken.Payload verifyToken(String idTokenString) throws Exception {
		if (clientId == null || clientId.isEmpty()) {
			log.error("Google Client ID not configured");
			throw new IllegalStateException("Google Client ID not configured");
		}

		try {
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
					new GsonFactory())
					.setAudience(Collections.singletonList(clientId))
					.build();

			GoogleIdToken idToken = verifier.verify(idTokenString);
			if (idToken == null) {
				log.warn("Google ID token verification failed: token is null");
				throw new SecurityException("Invalid Google ID token");
			}

			GoogleIdToken.Payload payload = idToken.getPayload();

			// Verify issuer
			String issuer = payload.getIssuer();
			if (!issuer.equals(GOOGLE_ISSUER_1) && !issuer.equals(GOOGLE_ISSUER_2)) {
				log.warn("Invalid Google token issuer: {}", issuer);
				throw new SecurityException("Invalid token issuer: " + issuer);
			}

			log.debug("Google token verified successfully for subject: {}", payload.getSubject());
			return payload;
		} catch (SecurityException e) {
			throw e;
		} catch (Exception e) {
			log.error("Error verifying Google token", e);
			throw new SecurityException("Failed to verify Google token: " + e.getMessage(), e);
		}
	}
}
