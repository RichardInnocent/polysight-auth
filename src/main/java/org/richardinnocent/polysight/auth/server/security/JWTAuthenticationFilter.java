package org.richardinnocent.polysight.auth.server.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.richardinnocent.polysight.auth.server.Qualifiers;
import org.richardinnocent.polysight.auth.server.http.controller.MissingParametersException;
import org.richardinnocent.polysight.auth.server.http.controller.MissingParametersException.Creator;
import org.richardinnocent.polysight.auth.server.models.user.AccountStatus;
import org.richardinnocent.polysight.auth.server.models.user.PolysightUser;
import org.richardinnocent.polysight.auth.server.services.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Authentication filter that will create a JWT and adds this to the response cookies list if
 * authentication is successful.
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private static final long EXPIRATION_TIME_MILLIS = 864_000_000L;
  private static final String EMAIL_KEY = "email";
  private static final String PASSWORD_KEY = "password";
  private static final ObjectMapper JWT_MAPPER = new ObjectMapper();

  private final AuthenticationManager authenticationManager;
  private final PublicPrivateKeyProvider keyProvider;
  private final UserService userService;

  public JWTAuthenticationFilter(
      AuthenticationManager authenticationManager,
      @Qualifier(Qualifiers.JWT) PublicPrivateKeyProvider keyProvider,
      UserService userService) {
    this.authenticationManager = authenticationManager;
    this.keyProvider = keyProvider;
    this.userService = userService;
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws AuthenticationException, MissingParametersException {
    Creator missingParamsCreator = MissingParametersException.creator();
    String email = missingParamsCreator.getOrLogMissing(EMAIL_KEY, request::getParameter);
    String password = missingParamsCreator.getOrLogMissing(PASSWORD_KEY, request::getParameter);
    missingParamsCreator.throwIfAnyMissing();

    Optional<PolysightUser> userOptional = userService.findByEmail(email);
    if (userOptional.isEmpty()) {
      throw new BadCredentialsException("Bad credentials");
    }
    PolysightUser user = userOptional.get();

    if (user.getAccountStatus() != AccountStatus.ACTIVE) {
      throw new DisabledException("Your account has been disabled");
    }

    return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            email, password + user.getPasswordSalt(), Collections.emptyList()
        )
    );
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication auth
  ) throws IOException, ServletException {
    super.successfulAuthentication(request, response, chain, auth);

    User user = (User) auth.getPrincipal();

    String token = JWT
        .create()
        .withIssuer(JwtFields.ISSUER)
        .withClaim(JwtFields.EMAIL_CLAIM_KEY, user.getUsername())
        .withClaim(JwtFields.AUTHORITIES_CLAIM_KEY, buildAuthoritiesClaimValue(user))
        .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS))
        .sign(
            Algorithm.ECDSA512(
                (ECPublicKey) keyProvider.getPublicKey(),
                (ECPrivateKey) keyProvider.getPrivateKey()
            )
        );

    Cookie jwtCookie = new Cookie(JwtFields.HEADER_NAME, token);
    jwtCookie.setHttpOnly(true);
    jwtCookie.setMaxAge((int) (EXPIRATION_TIME_MILLIS / 1000L));
    response.addCookie(jwtCookie);
  }

  private String buildAuthoritiesClaimValue(User user) {
    return buildAuthoritiesClaimValue(user.getAuthorities());
  }

  private String buildAuthoritiesClaimValue(Collection<GrantedAuthority> authorities) {
    if (authorities == null) {
      return "[]";
    }
    try {
      return JWT_MAPPER.writeValueAsString(
          authorities.stream()
                     .map(GrantedAuthority::getAuthority)
                     .collect(Collectors.toList()));
    } catch (JsonProcessingException e) {
      return "[]";
    }
  }

}
