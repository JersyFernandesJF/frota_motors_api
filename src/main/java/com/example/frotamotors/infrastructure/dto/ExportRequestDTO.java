package com.example.frotamotors.infrastructure.dto;

import java.util.Map;

public record ExportRequestDTO(String format, Map<String, Object> filters, java.util.List<String> fields) {}

