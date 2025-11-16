package com.example.frotamotors.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.frotamotors.infrastructure.security.JwtAuthenticationFilter;
import com.example.frotamotors.infrastructure.security.RateLimitingFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final RateLimitingFilter rateLimitingFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .headers(
            headers ->
                headers
                    .httpStrictTransportSecurity(
                        hstsConfig ->
                            hstsConfig
                                .maxAgeInSeconds(31536000)
                                .includeSubDomains(true))
                    .contentTypeOptions(contentTypeOptions -> {})
                    .frameOptions(frameOptions -> frameOptions.deny()))
        .authorizeHttpRequests(
            auth ->
                auth
                    // Swagger e documentação
                    .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html")
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
    configuration.setAllowCredentials(true);

    // Get allowed origins from environment variable or use default
    String allowedOrigins = System.getenv("CORS_ALLOWED_ORIGINS");
    if (allowedOrigins != null && !allowedOrigins.isEmpty()) {
      for (String origin : allowedOrigins.split(",")) {
        configuration.addAllowedOrigin(origin.trim());
      }
    } else {
      // Default: allow localhost for development and production domains
      configuration.addAllowedOrigin("http://localhost:3000");
      configuration.addAllowedOrigin("http://localhost:8080");
      configuration.addAllowedOrigin("http://localhost:9090");
      configuration.addAllowedOrigin("https://api.frotamotors.com");
      configuration.addAllowedOrigin("http://api.frotamotors.com");
    }

    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
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
