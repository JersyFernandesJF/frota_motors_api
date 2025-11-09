package com.example.frotamotors.domain.service;

import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.domain.model.UserContact;
import com.example.frotamotors.infrastructure.dto.UserContactDTO;
import com.example.frotamotors.infrastructure.mapper.UserContactMapper;
import com.example.frotamotors.infrastructure.persistence.UserContactRepository;
import com.example.frotamotors.infrastructure.persistence.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserContactService {

  @Autowired private UserContactRepository userContactRepository;

  @Autowired private UserRepository userRepository;

  public UserContactDTO create(UserContactDTO dto, UUID userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

    UserContact contact = UserContactMapper.toEntity(dto, user);
    contact.setUser(user);

    UserContact saved = userContactRepository.save(contact);
    return UserContactMapper.toResponse(saved);
  }

  public UserContactDTO getById(UUID id) {
    UserContact contact =
        userContactRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("UserContact not found"));
    return UserContactMapper.toResponse(contact);
  }

  public List<UserContactDTO> getByUser(UUID userId) {
    return userContactRepository.findByUserId(userId).stream()
        .map(UserContactMapper::toResponse)
        .collect(Collectors.toList());
  }

  public void delete(UUID id) {
    if (!userContactRepository.existsById(id)) {
      throw new EntityNotFoundException("UserContact not found");
    }
    userContactRepository.deleteById(id);
  }
}
