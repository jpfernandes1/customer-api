package com.neoaplicacoes.customerapi.security.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtUtilsTest {

  private JwtUtils jwtUtils;

  @BeforeEach
  void setUp() {
    jwtUtils = new JwtUtils("12345678901234567890123456789012", 3600000); // 32 chars + 1h
  }

  @Test
  void generateTokenAndGetUsername_validToken_success() {
    String token = jwtUtils.generateToken("test@example.com");
    assertNotNull(token);
    assertEquals("test@example.com", jwtUtils.getUsernameFromToken(token));
  }

  @Test
  void validateToken_validAndInvalidTokens() {
    String token = jwtUtils.generateToken("test@example.com");
    assertTrue(jwtUtils.validateToken(token));
    assertFalse(jwtUtils.validateToken("invalid.token.value"));
  }
}
