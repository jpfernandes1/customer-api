package com.neoaplicacoes.customerapi.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponseDTO(
    @JsonProperty("access_token") String accessToken,
    @JsonProperty("refresh_token") String refreshToken,
    @JsonProperty("token_type") String tokenType,
    @JsonProperty("user_id") Long userId,
    String email,
    String role,
    @JsonProperty("expires_in") Long expiresIn) {
  public LoginResponseDTO(
      String accessToken, String refreshToken, Long userId, String email, String role) {
    this(accessToken, refreshToken, "Bearer", userId, email, role, 3600L);
  }
}
