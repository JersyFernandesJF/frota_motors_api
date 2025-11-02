package com.example.kubico.infrastructure.mapper;

import com.example.kubico.domain.model.Agency;
import com.example.kubico.domain.model.User;
import com.example.kubico.infrastructure.dto.AgencyCreateDTO;
import com.example.kubico.infrastructure.dto.AgencyResponseDTO;

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
    return agency;
  }
}
