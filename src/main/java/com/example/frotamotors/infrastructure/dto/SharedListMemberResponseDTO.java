package com.example.frotamotors.infrastructure.dto;

import java.util.UUID;

public record SharedListMemberResponseDTO(UUID id, UUID listId, UUID userId) {}
