package com.example.kubico.domain.service;

import com.example.kubico.domain.model.User;
import com.example.kubico.infrastructure.dto.UserCreateDTO;
import com.example.kubico.infrastructure.dto.UserResponseDTO;
import com.example.kubico.infrastructure.mapper.UserMapper;
import com.example.kubico.infrastructure.dto.UserUpdateDTO;
import com.example.kubico.infrastructure.persistence.UserRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  public Page<User> getAllUsers(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  public User updateUser(UUID id, UserUpdateDTO dto) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("User not found"));

    // Check if email is being changed and if it's already taken
    if (!user.getEmail().equals(dto.email())) {
      if (userRepository.findByEmail(dto.email()).isPresent()) {
        throw new IllegalArgumentException("Email already exists");
      }
      user.setEmail(dto.email());
    }

    user.setName(dto.name());
    if (dto.imageUrl() != null) {
      user.setImageUrl(dto.imageUrl());
    }

    return userRepository.save(user);
  }

  public User updateUser(User user) {
    return userRepository.save(user);
  }

  public void deleteUser(UUID id) {
    userRepository.deleteById(id);
  }
}
