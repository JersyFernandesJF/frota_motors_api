package com.example.frotamotors.infrastructure.dto;

import java.time.LocalDateTime;

public record InspectionRescheduleRequestDTO(LocalDateTime newScheduledAt, String reason) {}
