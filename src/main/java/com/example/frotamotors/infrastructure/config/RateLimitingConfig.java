package com.example.frotamotors.infrastructure.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
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
    return Bucket.builder()
        .addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1))))
        .build();
  }

  // Rate limit: 20 requests per minute for general API endpoints
  @Bean(name = "apiRateLimiter")
  public Bucket apiRateLimiter() {
    return Bucket.builder()
        .addLimit(Bandwidth.classic(20, Refill.intervally(20, Duration.ofMinutes(1))))
        .build();
  }

  // Per-IP rate limiting storage
  private final Map<String, Bucket> ipBuckets = new ConcurrentHashMap<>();

  public Bucket resolveBucket(String ip) {
    return ipBuckets.computeIfAbsent(
        ip,
        key ->
            Bucket.builder()
                .addLimit(Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1))))
                .build());
  }
}

