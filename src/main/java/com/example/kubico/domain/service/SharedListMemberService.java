package com.example.kubico.domain.service;

import com.example.kubico.domain.model.SharedList;
import com.example.kubico.domain.model.SharedListMember;
import com.example.kubico.domain.model.User;
import com.example.kubico.infrastructure.dto.SharedListMemberCreateDTO;
import com.example.kubico.infrastructure.dto.SharedListMemberResponseDTO;
import com.example.kubico.infrastructure.mapper.SharedListMemberMapper;
import com.example.kubico.infrastructure.persistence.SharedListMemberRepository;
import com.example.kubico.infrastructure.persistence.SharedListRepository;
import com.example.kubico.infrastructure.persistence.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SharedListMemberService {

  @Autowired private SharedListMemberRepository sharedListMemberRepository;

  @Autowired private UserRepository userRepository;

  @Autowired private SharedListRepository sharedListRepository;

  public SharedListMemberResponseDTO create(SharedListMemberCreateDTO dto) {
    SharedList list =
        sharedListRepository
            .findById(dto.listId())
            .orElseThrow(() -> new EntityNotFoundException("SharedList not found"));

    User user =
        userRepository
            .findById(dto.userId())
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

    SharedListMember member = new SharedListMember();
    member.setList(list);
    member.setUser(user);

    SharedListMember saved = sharedListMemberRepository.save(member);
    return SharedListMemberMapper.toResponse(saved);
  }

  public List<SharedListMemberResponseDTO> getAll() {
    return sharedListMemberRepository.findAll().stream()
        .map(SharedListMemberMapper::toResponse)
        .collect(Collectors.toList());
  }

  public SharedListMemberResponseDTO getById(UUID id) {
    SharedListMember member =
        sharedListMemberRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("SharedListMember not found"));
    return SharedListMemberMapper.toResponse(member);
  }

  public void delete(UUID id) {
    if (!sharedListMemberRepository.existsById(id)) {
      throw new EntityNotFoundException("SharedListMember not found");
    }
    sharedListMemberRepository.deleteById(id);
  }
}
