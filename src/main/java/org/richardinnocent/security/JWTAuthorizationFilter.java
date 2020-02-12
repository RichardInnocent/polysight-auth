package org.richardinnocent.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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

  JWTAuthorizationFilter(AuthenticationManager authManager,
                         PublicPrivateKeyProvider keyProvider,
                         AuthenticationFacade authenticationFacade) {
    super(authManager);
    this.keyProvider = keyProvider;
    this.authenticationFacade = authenticationFacade;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req,
                                  HttpServletResponse res,
                                  FilterChain chain) throws IOException, ServletException {
    Optional<UsernamePasswordAuthenticationToken> authToken = getAuthentication(req.getCookies());
    if (authToken.isPresent()) {
      authenticationFacade.setAuthentication(authToken.get());
      if (req.getRequestURI().startsWith(req.getContextPath() + "/login")) {
        res.sendRedirect("/profile");
        return;
      }
    }
    chain.doFilter(req, res);
  }

  private Optional<UsernamePasswordAuthenticationToken> getAuthentication(Cookie[] cookies) {
    if (cookies == null) {
      return Optional.empty();
    }
    return Stream.of(cookies)
                 .filter(cookie -> JWTCookieFields.COOKIE_NAME.equals(cookie.getName()))
                 .findAny()
                 .map(this::getAuthentication);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(Cookie jwtCookie) {
    return getAuthentication(jwtCookie.getValue());
  }

  private UsernamePasswordAuthenticationToken getAuthentication(String cookieValue) {
    if (cookieValue != null) {
       return getAuthentication(decodeJwt(cookieValue));
    }
    return null;
  }

  private DecodedJWT decodeJwt(String token) {
    try {
      return
          JWT.require(
              Algorithm.ECDSA512((ECPublicKey) keyProvider.getPublicKey(),
                                 (ECPrivateKey) keyProvider.getPrivateKey()))
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
    String email = jwt.getClaim(JWTCookieFields.EMAIL_CLAIM_KEY).asString();
    if (email != null) {
      List<GrantedAuthority> authorities =
          getAuthorities(jwt.getClaim(JWTCookieFields.AUTHORITIES_CLAIM_KEY).asString());
      return new UsernamePasswordAuthenticationToken(email, null, authorities);
    }
    return null;
  }

  private List<GrantedAuthority> getAuthorities(String rawAuthorities) {
    return getAuthoritiesAsStrings(rawAuthorities).stream()
                                                  .map(text -> (GrantedAuthority) () -> text)
                                                  .collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  private List<String> getAuthoritiesAsStrings(String rawAuthorities) {
    if (rawAuthorities == null) {
      return Collections.emptyList();
    }
    try {
      return JWT_MAPPER.readValue(rawAuthorities, List.class);
    } catch (JsonProcessingException e) {
      LOGGER.warn("Could not parse authorities", e);
      return Collections.emptyList();
    }
  }

}
