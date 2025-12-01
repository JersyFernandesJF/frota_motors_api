package com.example.frotamotors.infrastructure.mapper;

import com.example.frotamotors.domain.model.SharedList;
import com.example.frotamotors.domain.model.SharedListItem;
import com.example.frotamotors.infrastructure.dto.SharedListItemResponseDTO;

public class SharedListItemMapper {

  private SharedListItemMapper() {}

  public static SharedListItemResponseDTO toResponse(SharedListItem item) {
    return new SharedListItemResponseDTO(item.getId(), item.getList().getId(), null);
  }

  public static SharedListItem toEntity(SharedList list) {
    SharedListItem item = new SharedListItem();
    item.setList(list);
    return item;
  }
}
