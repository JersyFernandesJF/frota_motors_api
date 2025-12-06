package com.example.frotamotors.infrastructure.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

@Configuration
public class AwsS3Config {

  @Value("${spring.s3.access.key:}")
  private String accessKey;

  @Value("${spring.s3.secret.key:}")
  private String secretKey;

  @Value("${spring.s3.region:us-east-1}")
  private String region;

  @Value("${spring.s3.endpoint:}")
  private String endpoint;

  @Bean
  public S3Client s3Client() {
    // Ensure region is not null or empty
    String regionValue = (region == null || region.trim().isEmpty()) ? "us-east-1" : region;
    var builder = S3Client.builder().region(Region.of(regionValue));

    // Use explicit credentials only if both are provided and non-empty
    // Otherwise, AWS SDK will use default credential provider chain (IAM roles, env vars, etc.)
    if (accessKey != null && !accessKey.isEmpty() && secretKey != null && !secretKey.isEmpty()) {
      builder.credentialsProvider(
          StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)));
    }

    // Configurar endpoint customizado se fornecido (para LocalStack)
    if (endpoint != null && !endpoint.isEmpty()) {
      builder.endpointOverride(URI.create(endpoint));
      // LocalStack requer path-style access
      builder.serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build());
    }

    return builder.build();
  }
}

