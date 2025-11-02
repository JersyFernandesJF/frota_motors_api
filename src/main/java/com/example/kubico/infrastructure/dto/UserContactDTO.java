package com.example.kubico.infrastructure.dto;

import com.example.kubico.domain.enums.ContactType;
import java.util.UUID;

public record UserContactDTO(
    UUID id, ContactType contactType, String contactValue, boolean primaryContact) {}
