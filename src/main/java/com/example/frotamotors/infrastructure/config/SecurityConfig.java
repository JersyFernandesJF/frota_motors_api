package com.example.frotamotors.infrastructure.config;

import com.example.frotamotors.infrastructure.security.JwtAuthenticationFilter;
import com.example.frotamotors.infrastructure.security.RateLimitingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final RateLimitingFilter rateLimitingFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors(c -> c.configurationSource(corsConfigurationSource()))
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(
						session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.headers(
						headers -> headers
								.contentTypeOptions(contentTypeOptions -> {
								})
								.frameOptions(frameOptions -> frameOptions.disable()))
				.authorizeHttpRequests(
						auth -> auth
								// Swagger e documentação
								.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
								.permitAll()
								// Autenticação
								.requestMatchers("/api/v1/auth/**")
								.permitAll()
								// Uploads e mídia pública
								.requestMatchers("/uploads/**", "/api/v1/media/**")
								.permitAll()
								// Endpoints GET públicos (listagem, busca, detalhes)
								.requestMatchers(
										HttpMethod.GET,
										"/api/v1/vehicles/**",
										"/api/v1/properties/**",
										"/api/v1/parts/**",
										"/api/v1/agencies/**",
										"/api/v1/reviews/**",
										"/api/v1/search/**",
										"/api/v1/locations/**")
								.permitAll()
								// Permitir acesso público ao GET de um usuário específico (para perfis de
								// vendedores)
								.requestMatchers(HttpMethod.GET, "/api/v1/users/*")
								.permitAll()
								// Permitir criação de conta (POST /api/v1/users) sem autenticação
								.requestMatchers(HttpMethod.POST, "/api/v1/users")
								.permitAll()
								// Endpoints específicos com roles
								.requestMatchers("/api/v1/users/**", "/api/v1/complaints/**")
								.hasAnyRole("ADMIN", "BUYER", "OWNER", "AGENT")
								// Demais endpoints requerem autenticação
								.anyRequest()
								.authenticated())
				.addFilterBefore(rateLimitingFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		// Allow specific origins (required when allowCredentials is true)
		configuration.addAllowedOrigin("https://www.frotamotors.com");
		configuration.addAllowedOrigin("https://frotamotors.com");
		configuration.addAllowedOrigin("http://localhost:5173");
		configuration.addAllowedOrigin("http://localhost:3000");
		configuration.addAllowedOrigin("http://localhost:8080");

		// Allow all methods explicitly
		configuration.addAllowedMethod(HttpMethod.GET);
		configuration.addAllowedMethod(HttpMethod.POST);
		configuration.addAllowedMethod(HttpMethod.PUT);
		configuration.addAllowedMethod(HttpMethod.DELETE);
		configuration.addAllowedMethod(HttpMethod.PATCH);
		configuration.addAllowedMethod(HttpMethod.OPTIONS);
		configuration.addAllowedMethod(HttpMethod.HEAD);

		// Allow all headers
		configuration.addAllowedHeader("*");

		// Allow credentials
		configuration.setAllowCredentials(true);

		// Allow preflight caching
		configuration.setMaxAge(3600L);

		// Expose Authorization header
		configuration.addExposedHeader("Authorization");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
