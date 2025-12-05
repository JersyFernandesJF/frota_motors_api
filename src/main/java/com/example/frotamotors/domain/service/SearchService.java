package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.enums.PartCategory;
import com.example.frotamotors.domain.enums.PartStatus;
import com.example.frotamotors.domain.enums.VehicleStatus;
import com.example.frotamotors.domain.enums.VehicleType;
import com.example.frotamotors.domain.model.Part;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.infrastructure.dto.PartResponseDTO;
import com.example.frotamotors.infrastructure.dto.SearchResultDTO;
import com.example.frotamotors.infrastructure.dto.VehicleResponseDTO;
import com.example.frotamotors.infrastructure.mapper.PartMapper;
import com.example.frotamotors.infrastructure.mapper.VehicleMapper;
import com.example.frotamotors.infrastructure.persistence.PartRepository;
import com.example.frotamotors.infrastructure.persistence.VehicleRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

  @Autowired private VehicleRepository vehicleRepository;

  @Autowired private PartRepository partRepository;

  public SearchResultDTO searchAll(
      // Vehicle filters
      VehicleType vehicleType,
      VehicleStatus vehicleStatus,
      BigDecimal vehicleMinPrice,
      BigDecimal vehicleMaxPrice,
      String vehicleBrand,
      String vehicleModel,
      Integer vehicleMinYear,
      Integer vehicleMaxYear,
      String vehicleFuelType,
      // Part filters
      PartCategory partCategory,
      PartStatus partStatus,
      BigDecimal partMinPrice,
      BigDecimal partMaxPrice,
      String partBrand,
      String partName,
      String partNumber,
      String partOemNumber) {

    // Search vehicles
    List<Vehicle> vehicles =
        vehicleRepository.search(
            vehicleType,
            vehicleStatus,
            vehicleMinPrice,
            vehicleMaxPrice,
            vehicleBrand,
            vehicleModel,
            vehicleMinYear,
            vehicleMaxYear,
            vehicleFuelType);
    List<VehicleResponseDTO> vehicleDTOs =
        vehicles.stream().map(VehicleMapper::toResponse).collect(Collectors.toList());

    // Search parts
    List<Part> parts =
        partRepository.search(
            partCategory,
            partStatus,
            partMinPrice,
            partMaxPrice,
            partBrand,
            partName,
            partNumber,
            partOemNumber);
    List<PartResponseDTO> partDTOs =
        parts.stream().map(PartMapper::toResponse).collect(Collectors.toList());

    return new SearchResultDTO(
        vehicleDTOs,
        partDTOs,
        (long) vehicleDTOs.size(),
        (long) partDTOs.size());
  }
}
