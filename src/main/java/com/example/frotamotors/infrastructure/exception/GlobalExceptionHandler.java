package com.example.frotamotors.infrastructure.exception;

import com.example.frotamotors.infrastructure.dto.ErrorResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponseDTO> handleEntityNotFound(
      EntityNotFoundException ex, WebRequest request) {
    log.error("Entity not found: {}", ex.getMessage());
    ErrorResponseDTO error =
        ErrorResponseDTO.of(
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", ""));
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponseDTO> handleIllegalArgument(
      IllegalArgumentException ex, WebRequest request) {
    log.error("Illegal argument: {}", ex.getMessage());
    ErrorResponseDTO error =
        ErrorResponseDTO.of(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", ""));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponseDTO> handleIllegalState(
      IllegalStateException ex, WebRequest request) {
    log.error("Illegal state: {}", ex.getMessage());
    ErrorResponseDTO error =
        ErrorResponseDTO.of(
            HttpStatus.CONFLICT.value(),
            "Conflict",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", ""));
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponseDTO> handleBadCredentials(
      BadCredentialsException ex, WebRequest request) {
    log.error("Bad credentials: {}", ex.getMessage());
    ErrorResponseDTO error =
        ErrorResponseDTO.of(
            HttpStatus.UNAUTHORIZED.value(),
            "Unauthorized",
            "Invalid email or password",
            request.getDescription(false).replace("uri=", ""));
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(
      MethodArgumentNotValidException ex, WebRequest request) {
    log.error("Validation error: {}", ex.getMessage());
    List<String> errors =
        ex.getBindingResult().getAllErrors().stream()
            .map(
                error -> {
                  String fieldName = ((FieldError) error).getField();
                  String errorMessage = error.getDefaultMessage();
                  return fieldName + ": " + errorMessage;
                })
            .collect(Collectors.toList());

    ErrorResponseDTO error =
        ErrorResponseDTO.of(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Failed",
            "Invalid input data",
            request.getDescription(false).replace("uri=", ""),
            errors);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponseDTO> handleConstraintViolation(
      ConstraintViolationException ex, WebRequest request) {
    log.error("Constraint violation: {}", ex.getMessage());
    List<String> errors =
        ex.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.toList());

    ErrorResponseDTO error =
        ErrorResponseDTO.of(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Failed",
            "Invalid input data",
            request.getDescription(false).replace("uri=", ""),
            errors);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(java.io.IOException.class)
  public ResponseEntity<ErrorResponseDTO> handleIOException(
      java.io.IOException ex, WebRequest request) {
    log.error("IO error: {}", ex.getMessage(), ex);
    
    // Check for S3/AWS errors
    String exceptionMessage = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
    String exceptionClass = ex.getClass().getSimpleName().toLowerCase();
    
    String message = ex.getMessage();
    if (exceptionMessage.contains("s3") || exceptionMessage.contains("aws") 
        || exceptionMessage.contains("bucket") || exceptionClass.contains("s3")) {
      if (exceptionMessage.contains("upload") || exceptionMessage.contains("putobject")) {
        message = "Erro ao fazer upload da imagem. Por favor, tente novamente.";
      } else if (exceptionMessage.contains("not authorized") || exceptionMessage.contains("403")) {
        message = "Erro de permissão ao acessar o armazenamento de imagens.";
      } else if (exceptionMessage.contains("not found") || exceptionMessage.contains("404")) {
        message = "Recurso de armazenamento não encontrado.";
      } else {
        message = "Erro ao acessar o serviço de armazenamento. Tente novamente mais tarde.";
      }
    }
    
    ErrorResponseDTO error =
        ErrorResponseDTO.of(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "IO Error",
            message,
            request.getDescription(false).replace("uri=", ""));
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex, WebRequest request) {
    log.error("Unexpected error: ", ex);
    ErrorResponseDTO error =
        ErrorResponseDTO.of(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "An unexpected error occurred",
            request.getDescription(false).replace("uri=", ""));
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
