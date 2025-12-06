package com.example.frotamotors.infrastructure.mapper;

import com.example.frotamotors.domain.model.Agency;
import com.example.frotamotors.domain.model.Media;
import com.example.frotamotors.domain.model.Part;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.infrastructure.dto.MediaResponseDTO;
import com.example.frotamotors.infrastructure.dto.PartCreateDTO;
import com.example.frotamotors.infrastructure.dto.PartResponseDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class PartMapper {

  private PartMapper() {}

  public static MediaResponseDTO toMediaResponseDTO(Media media) {
    if (media == null) return null;

    return new MediaResponseDTO(
        media.getId(), media.getMediaType(), media.getUrl(), media.getUploadedAt());
  }

  public static PartResponseDTO toResponse(Part part) {
    List<MediaResponseDTO> mediaList = null;

    if (part.getMedia() != null && !part.getMedia().isEmpty()) {
      mediaList =
          part.getMedia().stream().map(PartMapper::toMediaResponseDTO).collect(Collectors.toList());
    }

    return new PartResponseDTO(
        part.getId(),
        part.getSeller() != null ? UserMapper.toResponse(part.getSeller()) : null,
        part.getAgency() != null ? AgencyMapper.toResponse(part.getAgency()) : null,
        part.getCategory(),
        part.getStatus(),
        part.getName(),
        part.getDescription(),
        part.getPrice(),
        part.getCurrency(),
        part.getPartNumber(),
        part.getOemNumber(),
        part.getBrand(),
        part.getCompatibleVehicles(),
        part.getConditionType(),
        part.getQuantityAvailable(),
        part.getWarrantyMonths(),
        mediaList,
        part.getCreatedAt(),
        part.getUpdatedAt());
  }

  public static Part toEntity(PartCreateDTO dto, User seller, Agency agency) {
    Part part = new Part();
    part.setSeller(seller);
    part.setAgency(agency);
    part.setCategory(dto.category());
    part.setStatus(dto.status());
    part.setName(dto.name());
    part.setDescription(dto.description());
    part.setPrice(BigDecimal.valueOf(dto.price()));
    part.setCurrency(dto.currency().name());
    part.setPartNumber(dto.partNumber());
    part.setOemNumber(dto.oemNumber());
    part.setBrand(dto.brand());
    part.setCompatibleVehicles(dto.compatibleVehicles());
    part.setConditionType(dto.conditionType());
    part.setQuantityAvailable(dto.quantityAvailable());
    part.setWarrantyMonths(dto.warrantyMonths());
    return part;
  }
}
