package com.neoaplicacoes.customerapi.security.jwt;

import com.neoaplicacoes.customerapi.security.service.CustomUserDetailsService;
import com.neoaplicacoes.customerapi.security.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filter to authenticate requests using JWT. Runs once per request to validate the token and set
 * authentication in the SecurityContext.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private final JwtUtils jwtUtils;
  private final CustomUserDetailsService userDetailsService;

  @Autowired
  public JwtAuthenticationFilter(JwtUtils jwtUtils, CustomUserDetailsService userDetailsService) {
    this.jwtUtils = jwtUtils;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      String token = extractJwtFromRequest(request);

      if (token != null && jwtUtils.validateToken(token)) {
        String email = jwtUtils.getUsernameFromToken(token);

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
          UserDetails userDetails = userDetailsService.loadUserByUsername(email);

          UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }

    } catch (Exception ex) {
      logger.warn("Could not set user authentication in security context: {}", ex.getMessage());
      // Optional: respond with 401 if token is invalid or expired
      // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT token");
      // return;
    }

    filterChain.doFilter(request, response);
  }

  /**
   * Extracts the JWT token from the Authorization header.
   *
   * @param request HTTP request
   * @return JWT token string or null if not present
   */
  private String extractJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
