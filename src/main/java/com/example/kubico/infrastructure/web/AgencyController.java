package com.example.kubico.infrastructure.web;

import com.example.kubico.domain.model.Agency;
import com.example.kubico.domain.service.AgencyService;
import com.example.kubico.infrastructure.dto.AgencyCreateDTO;
import com.example.kubico.infrastructure.dto.AgencyResponseDTO;
import com.example.kubico.infrastructure.mapper.AgencyMapper;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/agencies")
@RequiredArgsConstructor
@Slf4j
public class AgencyController {

  private final AgencyService agencyService;

  @GetMapping
  public ResponseEntity<List<AgencyResponseDTO>> getAll() {
    List<Agency> agencies = agencyService.getAllAgencies();
    List<AgencyResponseDTO> response =
        agencies.stream().map(AgencyMapper::toResponse).collect(Collectors.toList());
    return ResponseEntity.ok(response);
  }

  @GetMapping("{id}")
  public ResponseEntity<AgencyResponseDTO> getById(@PathVariable UUID id) {
    Agency agency = agencyService.getAgencyById(id);
    return ResponseEntity.ok(AgencyMapper.toResponse(agency));
  }

  @PostMapping
  public ResponseEntity<AgencyResponseDTO> create(@RequestBody AgencyCreateDTO dto) {
    Agency agency = agencyService.createAgency(dto);
    return ResponseEntity.ok(AgencyMapper.toResponse(agency));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    agencyService.deleteAgency(id);
    return ResponseEntity.noContent().build();
  }
}
