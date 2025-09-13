package com.neoaplicacoes.customerapi.security.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
    @NotBlank(message = "Provide your email") String email,
    @NotBlank(message = "Provide your password") String password) {}
