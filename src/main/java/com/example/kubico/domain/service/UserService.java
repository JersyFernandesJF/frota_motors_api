package com.example.kubico.domain.service;

import com.example.kubico.domain.model.User;
import com.example.kubico.infrastructure.dto.UserCreateDTO;
import com.example.kubico.infrastructure.dto.UserResponseDTO;
import com.example.kubico.infrastructure.mapper.UserMapper;
import com.example.kubico.infrastructure.persistence.UserRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired private UserRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  public UserResponseDTO createUser(UserCreateDTO dto) {

    String encodedPassword = passwordEncoder.encode(dto.password());

    User user = UserMapper.toEntity(dto, encodedPassword);
    User savedUser = userRepository.save(user);

    return UserMapper.toResponse(savedUser);
  }

  public User getUserById(UUID id) {
    return (userRepository.findById(id).orElse(null));
  }

  public User updateUser(User user) {

    return userRepository.save(user);
  }

  public void deleteUser(UUID id) {
    userRepository.deleteById(id);
  }
}
