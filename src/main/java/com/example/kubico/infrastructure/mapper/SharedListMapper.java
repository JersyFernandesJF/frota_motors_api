package com.example.kubico.infrastructure.mapper;

import com.example.kubico.domain.model.SharedList;
import com.example.kubico.domain.model.User;
import com.example.kubico.infrastructure.dto.SharedListCreateDTO;
import com.example.kubico.infrastructure.dto.SharedListResponseDTO;

public class SharedListMapper {

  private SharedListMapper() {}

  public static SharedListResponseDTO toResponse(SharedList list) {
    return new SharedListResponseDTO(
        list.getId(), list.getOwner().getId(), list.getName(), list.getCreatedAt());
  }

  public static SharedList toEntity(SharedListCreateDTO dto, User owner) {
    SharedList list = new SharedList();
    list.setOwner(owner);
    list.setName(dto.name());
    return list;
  }
}
