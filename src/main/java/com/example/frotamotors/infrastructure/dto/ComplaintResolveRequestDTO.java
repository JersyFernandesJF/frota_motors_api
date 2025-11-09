package com.example.frotamotors.infrastructure.dto;

public record ComplaintResolveRequestDTO(
    String action, String notes, Boolean notifyReporter, Boolean notifySeller) {}

