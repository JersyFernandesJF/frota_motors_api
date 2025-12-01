package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.model.SharedList;
import com.example.frotamotors.domain.model.SharedListItem;
import com.example.frotamotors.infrastructure.dto.SharedListItemCreateDTO;
import com.example.frotamotors.infrastructure.dto.SharedListItemResponseDTO;
import com.example.frotamotors.infrastructure.mapper.SharedListItemMapper;
import com.example.frotamotors.infrastructure.persistence.SharedListItemRepository;
import com.example.frotamotors.infrastructure.persistence.SharedListRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SharedListItemService {

  @Autowired private SharedListItemRepository sharedListItemRepository;

  @Autowired private SharedListRepository sharedListRepository;

  public SharedListItemResponseDTO create(SharedListItemCreateDTO dto) {
    throw new UnsupportedOperationException("Shared lists for properties are no longer supported");
  }

  public List<SharedListItemResponseDTO> getAll() {
    return sharedListItemRepository.findAll().stream()
        .map(SharedListItemMapper::toResponse)
        .collect(Collectors.toList());
  }

  public SharedListItemResponseDTO getById(UUID id) {
    SharedListItem item =
        sharedListItemRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SharedListItem not found"));
    return SharedListItemMapper.toResponse(item);
  }

  public void delete(UUID id) {
    if (!sharedListItemRepository.existsById(id)) {
      throw new EntityNotFoundException("SharedListItem not found");
    }
    sharedListItemRepository.deleteById(id);
  }
}
