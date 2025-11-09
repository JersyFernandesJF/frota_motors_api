package com.example.frotamotors.infrastructure.mapper;

import com.example.frotamotors.domain.model.SharedList;
import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.infrastructure.dto.SharedListCreateDTO;
import com.example.frotamotors.infrastructure.dto.SharedListResponseDTO;

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
