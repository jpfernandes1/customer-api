package com.neoaplicacoes.customer_api.security.controller;

import com.neoaplicacoes.customer_api.model.dto.request.UserRequestDTO;
import com.neoaplicacoes.customer_api.model.dto.response.UserResponseDTO;
import com.neoaplicacoes.customer_api.security.util.JwtUtils;
import com.neoaplicacoes.customer_api.security.service.CustomUserDetailsService;
import com.neoaplicacoes.customer_api.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Login request")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          CustomUserDetailsService userDetailsService,
                          JwtUtils jwtUtils,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    /**
     * Endpoint to login user and return JWT token.
     *
     * @param request UserRequestDTO containing email and password
     * @return JWT token + user info
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserRequestDTO request) {
        try {
            // Authenticate email/password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
            String token = jwtUtils.generateToken(userDetails.getUsername());

            // Load user info for response
            UserResponseDTO userDto = userService.getByEmail(request.email()).get(0);

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", userDto);

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }
}
