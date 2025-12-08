package com.example.frotamotors.infrastructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.frotamotors.infrastructure.services.AwsS3Client;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/s3")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "S3 Storage", description = "Endpoints for file management in S3")
public class S3Controller {

  private final AwsS3Client awsS3Client;

  @Operation(
      summary = "Upload file to AWS S3",
      description = "Uploads a file to the S3 bucket and returns its public URL.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "File uploaded successfully",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid file or incorrect parameters",
            content = @Content),
        @ApiResponse(
            responseCode = "500",
            description = "Error during file upload",
            content = @Content)
      })
  @PostMapping("/upload")
  public ResponseEntity<String> uploadFile(
      Authentication authentication,
      @RequestBody(
              description = "File to be uploaded",
              required = true,
              content = @Content(mediaType = "multipart/form-data"))
          @RequestParam("file")
          MultipartFile file,
      @RequestParam(value = "identifier", required = false) String identifier) {
    try {
      // Use identifier if provided, otherwise use authentication name
      String id = identifier != null ? identifier : authentication.getName();
      String fileUrl = awsS3Client.uploadFile(file, id);
      log.info("File uploaded successfully: {}", fileUrl);
      return ResponseEntity.ok(fileUrl);
    } catch (Exception e) {
      log.error("Error uploading file: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Upload error: " + e.getMessage());
    }
  }

  @Operation(
      summary = "Delete file from AWS S3",
      description = "Deletes a file from the S3 bucket using the provided key.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "File deleted successfully",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid file key",
            content = @Content),
        @ApiResponse(
            responseCode = "500",
            description = "Error during file deletion",
            content = @Content)
      })
  @DeleteMapping
  public ResponseEntity<String> deleteFile(
      @RequestParam("key") @NotBlank String key, Authentication authentication) {
    try {
      awsS3Client.deleteFile(key);
      log.info("File deleted successfully: {}", key);
      return ResponseEntity.ok("File deleted successfully.");
    } catch (Exception e) {
      log.error("Error deleting file: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Deletion error: " + e.getMessage());
    }
  }

  @Operation(
      summary = "Delete file from AWS S3 by URL",
      description = "Deletes a file from the S3 bucket using its complete URL.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "File deleted successfully",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid URL",
            content = @Content),
        @ApiResponse(
            responseCode = "500",
            description = "Error during file deletion",
            content = @Content)
      })
  @DeleteMapping("/url")
  public ResponseEntity<String> deleteFileByUrl(
      @RequestParam("url") @NotBlank String url, Authentication authentication) {
    try {
      String key = awsS3Client.extractKeyFromUrl(url);
      if (key == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Invalid URL or not a valid S3 file.");
      }
      awsS3Client.deleteFile(key);
      log.info("File deleted successfully from URL: {}", url);
      return ResponseEntity.ok("File deleted successfully.");
    } catch (Exception e) {
      log.error("Error deleting file by URL: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Deletion error: " + e.getMessage());
    }
  }
}


