package com.example.kubico.domain.service;

import com.example.kubico.domain.model.Agency;
import com.example.kubico.domain.model.User;
import com.example.kubico.infrastructure.dto.AgencyCreateDTO;
import com.example.kubico.infrastructure.mapper.AgencyMapper;
import com.example.kubico.infrastructure.persistence.AgencyRepository;
import com.example.kubico.infrastructure.persistence.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AgencyService {

  @Autowired private AgencyRepository agencyRepository;

  @Autowired private UserRepository userRepository;

  public Agency createAgency(AgencyCreateDTO dto) {
    User owner =
        userRepository
            .findById(dto.userId())
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    Agency agency = AgencyMapper.toEntity(dto, owner);
    return agencyRepository.save(agency);
  }

  public List<Agency> getAllAgencies() {
    return agencyRepository.findAll();
  }

  public Page<Agency> getAllAgencies(Pageable pageable) {
    return agencyRepository.findAll(pageable);
  }

  public Agency getAgencyById(UUID id) {
    return agencyRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Agency not found"));
  }

  public void deleteAgency(UUID id) {
    agencyRepository.deleteById(id);
  }
}
