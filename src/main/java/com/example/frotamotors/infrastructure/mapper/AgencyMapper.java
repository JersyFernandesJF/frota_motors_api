package com.example.frotamotors.infrastructure.mapper;

import com.example.frotamotors.domain.model.Agency;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.infrastructure.dto.AgencyCreateDTO;
import com.example.frotamotors.infrastructure.dto.AgencyResponseDTO;

public class AgencyMapper {

  private AgencyMapper() {}

  public static AgencyResponseDTO toResponse(Agency agency) {
    return new AgencyResponseDTO(
        agency.getId(),
        agency.getAgencyName(),
        agency.getEmail(),
        agency.getLicenseNumber(),
        agency.getLogoUrl(),
        agency.getDescription(),
        agency.getWebsite(),
        agency.getSubscription() != null ? agency.getSubscription().getId() : null,
        agency.getIsActive(),
        agency.getCurrentVehicleCount(),
        agency.getPhone(),
        agency.getAddress(),
        agency.getTaxId(),
        agency.getCreatedAt(),
        agency.getUpdatedAt());
  }

  public static Agency toEntity(AgencyCreateDTO dto, User user) {
    Agency agency = new Agency();
    agency.setUser(user);
    agency.setAgencyName(dto.agencyName());
    agency.setLicenseNumber(dto.licenseNumber());
    agency.setLogoUrl(dto.logoUrl());
    agency.setDescription(dto.description());
    agency.setWebsite(dto.website());
    agency.setPhone(dto.phone());
    agency.setAddress(dto.address());
    agency.setTaxId(dto.taxId());
    return agency;
  }
}
