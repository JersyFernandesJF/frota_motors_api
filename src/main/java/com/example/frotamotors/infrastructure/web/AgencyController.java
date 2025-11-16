package com.example.frotamotors.infrastructure.web;

import com.example.frotamotors.domain.model.Agency;
import com.example.frotamotors.domain.service.AgencyService;
import com.example.frotamotors.infrastructure.dto.AgencyCreateDTO;
import com.example.frotamotors.infrastructure.dto.AgencyResponseDTO;
import com.example.frotamotors.infrastructure.dto.PageResponseDTO;
import com.example.frotamotors.infrastructure.mapper.AgencyMapper;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/agencies")
@RequiredArgsConstructor
@Slf4j
public class AgencyController {

  private final AgencyService agencyService;

  @GetMapping
  public ResponseEntity<PageResponseDTO<AgencyResponseDTO>> getAll(
      @PageableDefault(
              size = 20,
              sort = "createdAt",
              direction = org.springframework.data.domain.Sort.Direction.DESC)
          Pageable pageable) {
    Page<Agency> page = agencyService.getAllAgencies(pageable);

    List<AgencyResponseDTO> content =
        page.getContent().stream().map(AgencyMapper::toResponse).collect(Collectors.toList());

    PageResponseDTO<AgencyResponseDTO> response =
        PageResponseDTO.of(content, page.getNumber(), page.getSize(), page.getTotalElements());

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
