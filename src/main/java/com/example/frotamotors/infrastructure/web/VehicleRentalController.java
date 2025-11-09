package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.enums.RentalStatus;
import com.example.frotamotors.domain.model.VehicleRental;
import com.example.frotamotors.domain.service.VehicleRentalService;
import com.example.frotamotors.infrastructure.dto.PageResponseDTO;
import com.example.frotamotors.infrastructure.dto.VehicleRentalCreateDTO;
import com.example.frotamotors.infrastructure.dto.VehicleRentalResponseDTO;
import com.example.frotamotors.infrastructure.mapper.VehicleRentalMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/vehicle-rentals")
@RequiredArgsConstructor
@Slf4j
public class VehicleRentalController {

  @Autowired private VehicleRentalService vehicleRentalService;

  @GetMapping("/search")
  public ResponseEntity<PageResponseDTO<VehicleRentalResponseDTO>> search(
      @RequestParam(required = false) UUID renterId,
      @RequestParam(required = false) UUID vehicleId,
      @RequestParam(required = false) RentalStatus status,
      @RequestParam(required = false) LocalDate startDate,
      @RequestParam(required = false) LocalDate endDate,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {

    Page<VehicleRental> page =
        vehicleRentalService.search(renterId, vehicleId, status, startDate, endDate, pageable);

    List<VehicleRentalResponseDTO> content =
        page.getContent().stream()
            .map(VehicleRentalMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<VehicleRentalResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<PageResponseDTO<VehicleRentalResponseDTO>> getAll(
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<VehicleRental> page = vehicleRentalService.getAll(pageable);

    List<VehicleRentalResponseDTO> content =
        page.getContent().stream()
            .map(VehicleRentalMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<VehicleRentalResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/renter/{renterId}")
  public ResponseEntity<PageResponseDTO<VehicleRentalResponseDTO>> getByRenter(
      @PathVariable UUID renterId,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<VehicleRental> page = vehicleRentalService.getByRenter(renterId, pageable);

    List<VehicleRentalResponseDTO> content =
        page.getContent().stream()
            .map(VehicleRentalMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<VehicleRentalResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/vehicle/{vehicleId}")
  public ResponseEntity<PageResponseDTO<VehicleRentalResponseDTO>> getByVehicle(
      @PathVariable UUID vehicleId,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<VehicleRental> page = vehicleRentalService.getByVehicle(vehicleId, pageable);

    List<VehicleRentalResponseDTO> content =
        page.getContent().stream()
            .map(VehicleRentalMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<VehicleRentalResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/agency/{agencyId}")
  public ResponseEntity<PageResponseDTO<VehicleRentalResponseDTO>> getByAgency(
      @PathVariable UUID agencyId,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<VehicleRental> page = vehicleRentalService.getByAgency(agencyId, pageable);

    List<VehicleRentalResponseDTO> content =
        page.getContent().stream()
            .map(VehicleRentalMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<VehicleRentalResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("/status/{status}")
  public ResponseEntity<PageResponseDTO<VehicleRentalResponseDTO>> getByStatus(
      @PathVariable RentalStatus status,
      @PageableDefault(size = 20, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable) {
    Page<VehicleRental> page = vehicleRentalService.getByStatus(status, pageable);

    List<VehicleRentalResponseDTO> content =
        page.getContent().stream()
            .map(VehicleRentalMapper::toResponse)
            .collect(Collectors.toList());

    PageResponseDTO<VehicleRentalResponseDTO> response =
        PageResponseDTO.of(
            content, page.getNumber(), page.getSize(), page.getTotalElements());

    return ResponseEntity.ok(response);
  }

  @GetMapping("{id}")
  public ResponseEntity<VehicleRentalResponseDTO> getById(@PathVariable UUID id) {
    VehicleRental rental = vehicleRentalService.getById(id);
    return ResponseEntity.ok(VehicleRentalMapper.toResponse(rental));
  }

  @PostMapping
  public ResponseEntity<VehicleRentalResponseDTO> create(
      @RequestBody VehicleRentalCreateDTO dto) {
    VehicleRental saved = vehicleRentalService.create(dto);
    return ResponseEntity.ok(VehicleRentalMapper.toResponse(saved));
  }

  @PutMapping("{id}")
  public ResponseEntity<VehicleRentalResponseDTO> update(
      @PathVariable UUID id, @RequestBody VehicleRentalCreateDTO dto) {
    VehicleRental updated = vehicleRentalService.update(id, dto);
    return ResponseEntity.ok(VehicleRentalMapper.toResponse(updated));
  }

  @PostMapping("{id}/confirm")
  public ResponseEntity<VehicleRentalResponseDTO> confirmRental(@PathVariable UUID id) {
    VehicleRental rental = vehicleRentalService.confirmRental(id);
    return ResponseEntity.ok(VehicleRentalMapper.toResponse(rental));
  }

  @PostMapping("{id}/complete")
  public ResponseEntity<VehicleRentalResponseDTO> completeRental(
      @PathVariable UUID id, @RequestParam(required = false) Integer mileageAtReturn) {
    VehicleRental rental = vehicleRentalService.completeRental(id, mileageAtReturn);
    return ResponseEntity.ok(VehicleRentalMapper.toResponse(rental));
  }

  @PostMapping("{id}/cancel")
  public ResponseEntity<VehicleRentalResponseDTO> cancelRental(@PathVariable UUID id) {
    VehicleRental rental = vehicleRentalService.cancelRental(id);
    return ResponseEntity.ok(VehicleRentalMapper.toResponse(rental));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    vehicleRentalService.delete(id);
    return ResponseEntity.noContent().build();
  }
}

