package com.neoaplicacoes.customerapi.security.jwt;

import io.jsonwebtoken.*;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/** Provides JWT generation, validation, and extraction utilities. */
@Component
public class JwtTokenProvider {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private long jwtExpirationInMs;

  /**
   * Generate JWT token from user details.
   *
   * @param userDetails the authenticated user
   * @return signed JWT token
   */
  public String generateToken(UserDetails userDetails) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

    String roles =
        userDetails.getAuthorities().stream()
            .map(auth -> auth.getAuthority())
            .collect(Collectors.joining(","));

    return Jwts.builder()
        .setSubject(userDetails.getUsername())
        .claim("roles", roles)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  /**
   * Extract email from JWT token.
   *
   * @param token the JWT
   * @return email (subject)
   */
  public String getEmailFromToken(String token) {
    Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    return claims.getSubject();
  }

  /**
   * Validate the JWT token against user details.
   *
   * @param token the JWT
   * @param userDetails the user
   * @return true if valid
   */
  public boolean validateToken(String token, UserDetails userDetails) {
    try {
      Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

      String email = claims.getSubject();
      Date expiration = claims.getExpiration();

      return (email.equals(userDetails.getUsername()) && !expiration.before(new Date()));
    } catch (SignatureException
        | MalformedJwtException
        | ExpiredJwtException
        | UnsupportedJwtException
        | IllegalArgumentException ex) {
      return false;
    }
  }
}
