package org.richardinnocent.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.IOException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * This filter tries to find and validate a JWT token in cookies. If the token exists and is valid,
 * the user will be set as the authenticated principal.
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

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
    if (jwt != null) {
      String email = jwt.getClaim(JWTCookieFields.EMAIL_CLAIM_KEY).asString();
      if (email != null) {
        return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
      }
    }
    return null;
  }

}
