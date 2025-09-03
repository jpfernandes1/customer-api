package com.neoaplicacoes.customer_api.security.service;

import com.neoaplicacoes.customer_api.model.entity.User;
import com.neoaplicacoes.customer_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_userExists_returnsUserDetails() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPass");
        user.setRole("USER");
        user.setActive(true);

        when(userRepository.findByEmailIgnoreCase("test@example.com")).thenReturn(List.of(user));

        UserDetails userDetails = service.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("encodedPass", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void loadUserByUsername_userNotFound_throwsException() {
        when(userRepository.findByEmailIgnoreCase("unknown@example.com")).thenReturn(List.of());

        assertThrows(UsernameNotFoundException.class, () ->
                service.loadUserByUsername("unknown@example.com")
        );
    }
}
