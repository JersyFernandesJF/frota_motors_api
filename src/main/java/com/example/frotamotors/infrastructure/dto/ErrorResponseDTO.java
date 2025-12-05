package com.example.frotamotors.infrastructure.dto;

import java.util.List;

public record ErrorResponseDTO(
    Integer status,
    String title,
    String message,
    String path,
    String error,
    String code,
    List<String> errors) {

  // Simple constructor for OAuth errors (error, code)
  public ErrorResponseDTO(String error, String code) {
    this(null, null, null, null, error, code, null);
  }

  // Factory method for GlobalExceptionHandler (status, title, message, path)
  public static ErrorResponseDTO of(int status, String title, String message, String path) {
    return new ErrorResponseDTO(status, title, message, path, null, null, null);
  }

  // Factory method for validation errors (status, title, message, path, errors)
  public static ErrorResponseDTO of(
      int status, String title, String message, String path, List<String> errors) {
    return new ErrorResponseDTO(status, title, message, path, null, null, errors);
  }
}
