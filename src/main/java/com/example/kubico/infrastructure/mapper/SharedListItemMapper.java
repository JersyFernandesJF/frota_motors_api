package com.example.kubico.infrastructure.mapper;

import com.example.kubico.domain.model.Property;
import com.example.kubico.domain.model.SharedList;
import com.example.kubico.domain.model.SharedListItem;
import com.example.kubico.infrastructure.dto.SharedListItemResponseDTO;

public class SharedListItemMapper {

  private SharedListItemMapper() {}

  public static SharedListItemResponseDTO toResponse(SharedListItem item) {
    return new SharedListItemResponseDTO(
        item.getId(), item.getList().getId(), item.getProperty().getId());
  }

  public static SharedListItem toEntity(SharedList list, Property property) {
    SharedListItem item = new SharedListItem();
    item.setList(list);
    item.setProperty(property);
    return item;
  }
}
