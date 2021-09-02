package org.richardinnocent.polysight.auth.server.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * This filter tries to find and validate a JWT token in cookies. If the token exists and is valid,
 * the user will be set as the authenticated principal.
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthorizationFilter.class);
  private static final ObjectMapper JWT_MAPPER = new ObjectMapper();

  private final PublicPrivateKeyProvider keyProvider;
  private final AuthenticationFacade authenticationFacade;

  JWTAuthorizationFilter(
      AuthenticationManager authManager,
      PublicPrivateKeyProvider keyProvider,
      AuthenticationFacade authenticationFacade
  ) {
    super(authManager);
    this.keyProvider = keyProvider;
    this.authenticationFacade = authenticationFacade;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest req,
      HttpServletResponse res,
      FilterChain chain
  ) throws IOException, ServletException {
    getAuthentication(req.getHeader(JwtFields.HEADER_NAME))
        .ifPresent(authenticationFacade::setAuthentication);
    chain.doFilter(req, res);
  }

  private Optional<UsernamePasswordAuthenticationToken> getAuthentication(
      String authenticationHeader
  ) {
    return Optional
        .ofNullable(authenticationHeader)
        .map(header -> header.replace("Bearer ", ""))
        .map(header -> getAuthentication(decodeJwt(header)));
  }

  private DecodedJWT decodeJwt(String token) {
    try {
      return JWT
          .require(
              Algorithm.ECDSA512(
                  (ECPublicKey) keyProvider.getPublicKey(),
                  (ECPrivateKey) keyProvider.getPrivateKey()
              )
          )
          .build()
          .verify(token);
    } catch (JWTVerificationException e) {
      return null;
    }
  }

  private UsernamePasswordAuthenticationToken getAuthentication(DecodedJWT jwt) {
    if (jwt == null) {
      return null;
    }
    SimpleAuthenticatedUser user = SimpleAuthenticatedUser.of(
        jwt.getClaim(JwtFields.USER_ID_CLAIM_KEY).asLong(),
        jwt.getClaim(JwtFields.EMAIL_CLAIM_KEY).asString()
    );
    List<GrantedAuthority> authorities =
        getAuthorities(jwt.getClaim(JwtFields.AUTHORITIES_CLAIM_KEY).asString());
    return new UsernamePasswordAuthenticationToken(user, null, authorities);
  }

  private List<GrantedAuthority> getAuthorities(String rawAuthorities) {
    return getAuthoritiesAsStrings(rawAuthorities)
        .stream()
        .map(text -> (GrantedAuthority) () -> text)
        .collect(Collectors.toList());
  }

  private List<String> getAuthoritiesAsStrings(String rawAuthorities) {
    if (rawAuthorities == null) {
      return Collections.emptyList();
    }
    try {
      return JWT_MAPPER.readValue(rawAuthorities, new TypeReference<>(){});
    } catch (JsonProcessingException e) {
      LOGGER.warn("Could not parse authorities", e);
      return Collections.emptyList();
    }
  }

}
