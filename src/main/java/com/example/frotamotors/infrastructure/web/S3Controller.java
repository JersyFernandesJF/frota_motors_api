package com.example.frotamotors.infrastructure.web;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/s3")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "S3 Storage", description = "Endpoints para gerenciamento de arquivos no S3")
public class S3Controller {

  private final AwsS3Client awsS3Client;

  @Operation(
      summary = "Upload de arquivo para AWS S3",
      description = "Faz upload de um arquivo para o bucket S3 e retorna sua URL pública.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Arquivo enviado com sucesso",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
        @ApiResponse(
            responseCode = "400",
            description = "Arquivo inválido ou parâmetros incorretos",
            content = @Content),
        @ApiResponse(
            responseCode = "500",
            description = "Erro durante o upload do arquivo",
            content = @Content)
      })
  @PostMapping("/upload")
  public ResponseEntity<String> uploadFile(
      Authentication authentication,
      @RequestBody(
              description = "Arquivo a ser enviado",
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
          .body("Erro no upload: " + e.getMessage());
    }
  }

  @Operation(
      summary = "Deletar arquivo do AWS S3",
      description = "Deleta um arquivo do bucket S3 usando a chave fornecida.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Arquivo deletado com sucesso",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
        @ApiResponse(
            responseCode = "400",
            description = "Chave de arquivo inválida",
            content = @Content),
        @ApiResponse(
            responseCode = "500",
            description = "Erro durante a deleção do arquivo",
            content = @Content)
      })
  @DeleteMapping
  public ResponseEntity<String> deleteFile(
      @RequestParam("key") @NotBlank String key, Authentication authentication) {
    try {
      awsS3Client.deleteFile(key);
      log.info("File deleted successfully: {}", key);
      return ResponseEntity.ok("Arquivo deletado com sucesso.");
    } catch (Exception e) {
      log.error("Error deleting file: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Erro na deleção: " + e.getMessage());
    }
  }

  @Operation(
      summary = "Deletar arquivo do AWS S3 por URL",
      description = "Deleta um arquivo do bucket S3 usando sua URL completa.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Arquivo deletado com sucesso",
            content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
        @ApiResponse(
            responseCode = "400",
            description = "URL inválida",
            content = @Content),
        @ApiResponse(
            responseCode = "500",
            description = "Erro durante a deleção do arquivo",
            content = @Content)
      })
  @DeleteMapping("/url")
  public ResponseEntity<String> deleteFileByUrl(
      @RequestParam("url") @NotBlank String url, Authentication authentication) {
    try {
      String key = awsS3Client.extractKeyFromUrl(url);
      if (key == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("URL inválida ou não é um arquivo S3 válido.");
      }
      awsS3Client.deleteFile(key);
      log.info("File deleted successfully from URL: {}", url);
      return ResponseEntity.ok("Arquivo deletado com sucesso.");
    } catch (Exception e) {
      log.error("Error deleting file by URL: {}", e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("Erro na deleção: " + e.getMessage());
    }
  }
}

