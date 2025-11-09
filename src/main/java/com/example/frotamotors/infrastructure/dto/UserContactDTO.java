package com.example.frotamotors.infrastructure.dto;

import com.example.frotamotors.domain.enums.ContactType;
import java.util.UUID;

public record UserContactDTO(
    UUID id, ContactType contactType, String contactValue, boolean primaryContact) {}
