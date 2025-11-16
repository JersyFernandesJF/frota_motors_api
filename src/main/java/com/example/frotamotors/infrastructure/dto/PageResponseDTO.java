package com.example.frotamotors.infrastructure.dto;

import java.util.List;

public record PageResponseDTO<T>(
    List<T> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last) {
  public static <T> PageResponseDTO<T> of(List<T> content, int page, int size, long totalElements) {
    int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
    return new PageResponseDTO<>(
        content, page, size, totalElements, totalPages, page == 0, page >= totalPages - 1);
  }
}
