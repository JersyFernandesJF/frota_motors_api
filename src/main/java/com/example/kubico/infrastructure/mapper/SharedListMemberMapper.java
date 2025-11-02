package com.example.kubico.infrastructure.mapper;

import com.example.kubico.domain.model.SharedList;
import com.example.kubico.domain.model.SharedListMember;
import com.example.kubico.domain.model.User;
import com.example.kubico.infrastructure.dto.SharedListMemberResponseDTO;

public class SharedListMemberMapper {

  private SharedListMemberMapper() {}

  public static SharedListMemberResponseDTO toResponse(SharedListMember member) {
    return new SharedListMemberResponseDTO(
        member.getId(), member.getList().getId(), member.getUser().getId());
  }

  public static SharedListMember toEntity(SharedList list, User user) {
    SharedListMember member = new SharedListMember();
    member.setList(list);
    member.setUser(user);
    return member;
  }
}
