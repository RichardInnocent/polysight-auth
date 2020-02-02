package org.richardinnocent.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.richardinnocent.http.controller.MissingParametersException;
import org.richardinnocent.http.controller.MissingParametersException.Creator;
import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.persistence.user.PolysightUserDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
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

  private final AuthenticationManager authenticationManager;
  private final PublicPrivateKeyProvider keyProvider;
  private final PolysightUserDAO userDAO;

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager,
                                 @Qualifier("jwt") PublicPrivateKeyProvider keyProvider,
                                 PolysightUserDAO userDAO) {
    this.authenticationManager = authenticationManager;
    this.keyProvider = keyProvider;
    this.userDAO = userDAO;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
                                              HttpServletResponse response)
      throws AuthenticationException, MissingParametersException {
    Creator missingParamsCreator =
        MissingParametersException.creator();
    String email = missingParamsCreator.getOrLogMissing(EMAIL_KEY, request::getParameter);
    String password = missingParamsCreator.getOrLogMissing(PASSWORD_KEY, request::getParameter);
    missingParamsCreator.throwIfAnyMissing();

    Optional<PolysightUser> user = userDAO.findByEmail(email);
    if (user.isEmpty()) {
      throw new BadCredentialsException("Bad credentials");
    }
    return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            email, password + user.get().getPasswordSalt(), Collections.emptyList()));
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request,
                                          HttpServletResponse response,
                                          FilterChain chain,
                                          Authentication auth) {
    String token =
        JWT.create()
           .withIssuer(JWTCookieFields.ISSUER)
           .withClaim(JWTCookieFields.EMAIL_CLAIM_KEY, ((User) auth.getPrincipal()).getUsername())
           .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS))
           .sign(Algorithm.ECDSA512((ECPublicKey) keyProvider.getPublicKey(),
                                    (ECPrivateKey) keyProvider.getPrivateKey()));

    Cookie jwtCookie = new Cookie(JWTCookieFields.COOKIE_NAME, token);
    jwtCookie.setHttpOnly(true);
    jwtCookie.setMaxAge((int) (EXPIRATION_TIME_MILLIS / 1000L));
    response.addCookie(jwtCookie);
  }

}
