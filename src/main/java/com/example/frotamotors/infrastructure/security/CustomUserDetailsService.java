package com.example.frotamotors.infrastructure.security;

import com.example.frotamotors.domain.model.User;
import com.example.frotamotors.infrastructure.persistence.UserRepository;
import java.util.Collections;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

    return new CustomUserDetails(user);
  }

  public CustomUserDetails loadUserById(UUID id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));

    return new CustomUserDetails(user);
  }

  public static class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
      this.user = user;
    }

    @Override
    public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority>
        getAuthorities() {
      return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
      return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
      return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
      return true;
    }

    @Override
    public boolean isAccountNonLocked() {
      return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
      return true;
    }

    @Override
    public boolean isEnabled() {
      return true;
    }

    public User getUser() {
      return user;
    }

    public UUID getUserId() {
      return user.getId();
    }
  }
}
