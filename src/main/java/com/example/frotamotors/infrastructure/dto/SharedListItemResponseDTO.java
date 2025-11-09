package com.example.frotamotors.infrastructure.dto;

import java.util.UUID;

public record SharedListItemResponseDTO(UUID id, UUID listId, UUID propertyId) {}
