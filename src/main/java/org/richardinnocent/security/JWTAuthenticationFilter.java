package org.richardinnocent.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private static final long EXPIRATION_TIME = 864_000_000L;

  private final AuthenticationManager authenticationManager;
  private final PublicPrivateKeyProvider publicPrivateKeyProvider;

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                 PublicPrivateKeyProvider publicPrivateKeyProvider) {
    this.authenticationManager = authenticationManager;
    this.publicPrivateKeyProvider = publicPrivateKeyProvider;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Authentication attemptAuthentication(HttpServletRequest request,
                                              HttpServletResponse response)
      throws AuthenticationException {
    try {
      Map<String, String> credentials =
          new ObjectMapper().readValue(request.getInputStream(), Map.class);

      return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              credentials.get("username"),
              credentials.get("password"),
              Collections.emptyList()));
    } catch (IOException e) {
      throw new RuntimeException("Could not get credentials from request", e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request,
                                          HttpServletResponse response,
                                          FilterChain chain,
                                          Authentication auth)
      throws IOException, ServletException {
    String token = JWT.create()
                      .withIssuer("polysight")
                      .withClaim("username", ((User) auth.getPrincipal()).getUsername())
                      .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                      .sign(Algorithm.(new RSAP))
    response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
  }

}
