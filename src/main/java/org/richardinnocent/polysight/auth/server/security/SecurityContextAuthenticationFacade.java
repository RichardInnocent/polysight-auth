package org.richardinnocent.polysight.auth.server.security;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Manages the authentication object via the {@link SecurityContextHolder} class.
 */
@Component
public class SecurityContextAuthenticationFacade implements AuthenticationFacade {

  @Override
  public Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  public Optional<SimpleAuthenticatedUser> getAuthenticatedUser() {
    return Optional
        .ofNullable(getAuthentication())
        .map(Authentication::getPrincipal)
        .filter(SimpleAuthenticatedUser.class::isInstance)
        .map(SimpleAuthenticatedUser.class::cast);
  }

  @Override
  public void setAuthentication(Authentication authentication) {
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
