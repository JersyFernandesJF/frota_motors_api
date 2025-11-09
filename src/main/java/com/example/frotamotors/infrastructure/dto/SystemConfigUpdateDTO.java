package com.example.frotamotors.infrastructure.dto;

import java.util.Map;

public record SystemConfigUpdateDTO(Map<String, Object> value, String description) {}

