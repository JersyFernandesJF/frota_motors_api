package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.enums.PartCategory;
import com.example.frotamotors.domain.enums.PartStatus;
import com.example.frotamotors.domain.enums.VehicleStatus;
import com.example.frotamotors.domain.enums.VehicleType;
import com.example.frotamotors.domain.model.Part;
import com.example.frotamotors.domain.model.Property;
import com.example.frotamotors.domain.model.Vehicle;
import com.example.frotamotors.infrastructure.dto.PartResponseDTO;
import com.example.frotamotors.infrastructure.dto.PropertyResponseDTO;
import com.example.frotamotors.infrastructure.dto.SearchResultDTO;
import com.example.frotamotors.infrastructure.dto.VehicleResponseDTO;
import com.example.frotamotors.infrastructure.mapper.PartMapper;
import com.example.frotamotors.infrastructure.mapper.PropertyMapper;
import com.example.frotamotors.infrastructure.mapper.VehicleMapper;
import com.example.frotamotors.infrastructure.persistence.PartRepository;
import com.example.frotamotors.infrastructure.persistence.PropertyRepository;
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

  @Autowired private PropertyRepository propertyRepository;

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
      String partOemNumber,
      // Property filters
      Double propertyMinArea,
      Double propertyMaxArea,
      List<String> propertyTypes,
      Integer propertyBathrooms,
      Integer propertyRooms,
      Integer propertyFloors,
      Integer propertyYear) {

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

    // Search properties
    List<Property> properties =
        propertyRepository
            .findByAreaM2BetweenAndTypeInAndBathroomsGreaterThanEqualAndRoomsGreaterThanEqualAndTotalFloorsGreaterThanEqualAndYearBuiltGreaterThanEqual(
                propertyMinArea != null ? propertyMinArea : 0.0,
                propertyMaxArea != null ? propertyMaxArea : Double.MAX_VALUE,
                propertyTypes != null && !propertyTypes.isEmpty()
                    ? propertyTypes
                    : List.of("FOR_RENT", "FOR_SALE"),
                propertyBathrooms != null ? propertyBathrooms : 0,
                propertyRooms != null ? propertyRooms : 0,
                propertyFloors != null ? propertyFloors : 0,
                propertyYear != null ? propertyYear : 0);
    List<PropertyResponseDTO> propertyDTOs =
        properties.stream().map(PropertyMapper::toResponse).collect(Collectors.toList());

    return new SearchResultDTO(
        vehicleDTOs,
        partDTOs,
        propertyDTOs,
        (long) vehicleDTOs.size(),
        (long) partDTOs.size(),
        (long) propertyDTOs.size());
  }
}

