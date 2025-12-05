package com.example.frotamotors.infrastructure.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimitingConfig {

  // Rate limit: 5 requests per minute for authentication endpoints
  @Bean(name = "authRateLimiter")
  public Bucket authRateLimiter() {
    return Bucket.builder().addLimit(Bandwidth.simple(15, Duration.ofMinutes(1))).build();
  }

  // Rate limit: 100 requests per minute for general API endpoints (per instance)
  @Bean(name = "apiRateLimiter")
  public Bucket apiRateLimiter() {
    return Bucket.builder().addLimit(Bandwidth.simple(100, Duration.ofMinutes(1))).build();
  }

  // Per-IP rate limiting storage
  private final Map<String, Bucket> ipBuckets = new ConcurrentHashMap<>();

  public Bucket resolveBucket(String ip) {
    return ipBuckets.computeIfAbsent(
        ip,
        key ->
            Bucket.builder()
                // Per-IP: 60 requests por minuto, suficiente para navegação normal
                .addLimit(Bandwidth.simple(60, Duration.ofMinutes(1)))
                .build());
  }
}
