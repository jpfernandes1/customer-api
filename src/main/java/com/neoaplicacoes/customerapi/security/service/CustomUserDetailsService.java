package com.neoaplicacoes.customerapi.security.service;

import com.neoaplicacoes.customerapi.model.entity.User;
import com.neoaplicacoes.customerapi.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of Spring Security's UserDetailsService. Loads user info from the database
 * and maps it to UserDetails with proper roles and account flags.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Load user by email (username).
   *
   * @param email the user's email
   * @return UserDetails with authentication info
   * @throws UsernameNotFoundException if user not found
   */
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user =
        userRepository.findByEmailIgnoreCase(email).stream()
            .findFirst()
            .orElseThrow(
                () -> new UsernameNotFoundException("User not found with email: " + email));

    // Map role(s) to GrantedAuthority
    List<GrantedAuthority> authorities =
        List.of(user.getRole()).stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getEmail())
        .password(user.getPassword())
        .authorities(authorities)
        .accountExpired(false)
        .accountLocked(!user.getActive()) // Block login if user inactive
        .credentialsExpired(false)
        .disabled(!user.getActive()) // Disable account if inactive
        .build();
  }
}
