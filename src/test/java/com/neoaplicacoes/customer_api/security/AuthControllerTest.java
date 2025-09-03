package com.neoaplicacoes.customer_api.security;

import com.neoaplicacoes.customer_api.model.dto.request.UserRequestDTO;
import com.neoaplicacoes.customer_api.model.dto.response.UserResponseDTO;
import com.neoaplicacoes.customer_api.security.controller.AuthController;
import com.neoaplicacoes.customer_api.security.service.CustomUserDetailsService;
import com.neoaplicacoes.customer_api.security.util.JwtUtils;
import com.neoaplicacoes.customer_api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_success_returnsTokenAndUser() {
        // Arrange
        UserRequestDTO request = new UserRequestDTO("test@email.com", "password");

        List<UserResponseDTO> mockUsers = List.of(
                new UserResponseDTO(
                        1L,
                        "test@email.com",
                        "USER",
                        true,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );

        // Mocks
        when(userService.getByEmail(anyString())).thenReturn(mockUsers);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(
                new org.springframework.security.core.userdetails.User(
                        "test@email.com",
                        "encoded-password",
                        List.of(new SimpleGrantedAuthority("USER"))
                )
        );
        when(jwtUtils.generateToken(anyString())).thenReturn("mock-token");

        // Act
        ResponseEntity<?> response = authController.login(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("mock-token", body.get("token"));
        assertNotNull(body.get("user"));
    }

    @Test
    void login_invalidCredentials_returns401() {
        // Arrange
        UserRequestDTO request = new UserRequestDTO("wrong@email.com", "badpass");

        // Mock a failed authentication
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act
        ResponseEntity<?> response = authController.login(request);

        // Assert
        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Invalid email or password", response.getBody());
    }
}
