package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.enums.PartCategory;
import com.example.frotamotors.domain.enums.PartStatus;
import com.example.frotamotors.domain.enums.VehicleStatus;
import com.example.frotamotors.domain.enums.VehicleType;
import com.example.frotamotors.domain.service.SearchService;
import com.example.frotamotors.infrastructure.dto.SearchResultDTO;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/v1/search")
@RequiredArgsConstructor
@Slf4j
public class SearchController {

  @Autowired private SearchService searchService;

  @GetMapping("/all")
  public ResponseEntity<SearchResultDTO> searchAll(
      // Vehicle filters
      @RequestParam(required = false) VehicleType vehicleType,
      @RequestParam(required = false) VehicleStatus vehicleStatus,
      @RequestParam(required = false) BigDecimal vehicleMinPrice,
      @RequestParam(required = false) BigDecimal vehicleMaxPrice,
      @RequestParam(required = false) String vehicleBrand,
      @RequestParam(required = false) String vehicleModel,
      @RequestParam(required = false) Integer vehicleMinYear,
      @RequestParam(required = false) Integer vehicleMaxYear,
      @RequestParam(required = false) String vehicleFuelType,
      // Part filters
      @RequestParam(required = false) PartCategory partCategory,
      @RequestParam(required = false) PartStatus partStatus,
      @RequestParam(required = false) BigDecimal partMinPrice,
      @RequestParam(required = false) BigDecimal partMaxPrice,
      @RequestParam(required = false) String partBrand,
      @RequestParam(required = false) String partName,
      @RequestParam(required = false) String partNumber,
      @RequestParam(required = false) String partOemNumber,
      // Property filters
      @RequestParam(required = false) Double propertyMinArea,
      @RequestParam(required = false) Double propertyMaxArea,
      @RequestParam(required = false) List<String> propertyTypes,
      @RequestParam(required = false) Integer propertyBathrooms,
      @RequestParam(required = false) Integer propertyRooms,
      @RequestParam(required = false) Integer propertyFloors,
      @RequestParam(required = false) Integer propertyYear) {

    SearchResultDTO result =
        searchService.searchAll(
            vehicleType,
            vehicleStatus,
            vehicleMinPrice,
            vehicleMaxPrice,
            vehicleBrand,
            vehicleModel,
            vehicleMinYear,
            vehicleMaxYear,
            vehicleFuelType,
            partCategory,
            partStatus,
            partMinPrice,
            partMaxPrice,
            partBrand,
            partName,
            partNumber,
            partOemNumber,
            propertyMinArea,
            propertyMaxArea,
            propertyTypes,
            propertyBathrooms,
            propertyRooms,
            propertyFloors,
            propertyYear);

    return ResponseEntity.ok(result);
  }
}
