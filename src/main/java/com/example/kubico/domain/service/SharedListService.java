package com.example.kubico.domain.service;

import com.example.kubico.domain.model.SharedList;
import com.example.kubico.domain.model.User;
import com.example.kubico.infrastructure.dto.SharedListCreateDTO;
import com.example.kubico.infrastructure.dto.SharedListResponseDTO;
import com.example.kubico.infrastructure.mapper.SharedListMapper;
import com.example.kubico.infrastructure.persistence.SharedListRepository;
import com.example.kubico.infrastructure.persistence.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SharedListService {

  @Autowired private SharedListRepository sharedListRepository;

  @Autowired private UserRepository userRepository;

  public SharedListResponseDTO create(SharedListCreateDTO dto) {
    User owner =
        userRepository
            .findById(dto.ownerId())
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

    SharedList list = new SharedList();
    list.setName(dto.name());
    list.setOwner(owner);

    SharedList saved = sharedListRepository.save(list);
    return SharedListMapper.toResponse(saved);
  }

  public List<SharedListResponseDTO> getAll() {
    return sharedListRepository.findAll().stream()
        .map(SharedListMapper::toResponse)
        .collect(Collectors.toList());
  }

  public SharedListResponseDTO getById(UUID id) {
    SharedList list =
        sharedListRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SharedList not found"));
    return SharedListMapper.toResponse(list);
  }

  public void delete(UUID id) {
    if (!sharedListRepository.existsById(id)) {
      throw new EntityNotFoundException("SharedList not found");
    }
    sharedListRepository.deleteById(id);
  }
}
