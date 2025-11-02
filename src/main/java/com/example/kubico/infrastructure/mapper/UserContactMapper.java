package com.example.kubico.infrastructure.mapper;

import com.example.kubico.domain.model.User;
import com.example.kubico.domain.model.UserContact;
import com.example.kubico.infrastructure.dto.UserContactDTO;

public class UserContactMapper {

  private UserContactMapper() {}

  public static UserContactDTO toResponse(UserContact contact) {
    return new UserContactDTO(
        contact.getId(),
        contact.getContactType(),
        contact.getContactValue(),
        contact.getIsPrimary() != null && contact.getIsPrimary());
  }

  public static UserContact toEntity(UserContactDTO dto, User user) {
    UserContact contact = new UserContact();
    contact.setUser(user);
    contact.setContactType(dto.contactType());
    contact.setContactValue(dto.contactValue());
    contact.setIsPrimary(dto.primaryContact());
    return contact;
  }
}
