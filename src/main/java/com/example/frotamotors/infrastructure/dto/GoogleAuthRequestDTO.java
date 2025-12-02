package com.example.frotamotors.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Payload de autenticação Google espelhando o fluxo do projeto target.
 * <p>
 * O backend utiliza principalmente o {@code idToken} para validação junto ao
 * Google,
 * enquanto os demais campos permitem futuras validações/armazenamento de dados
 * de perfil.
 */
public record GoogleAuthRequestDTO(
		@NotBlank String idToken,
		@NotBlank String email,
		@NotBlank String givenName,
		String familyName,
		@NotBlank String googleId,
		String picture) {
}
