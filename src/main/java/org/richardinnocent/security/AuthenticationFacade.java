package org.richardinnocent.security;

import org.springframework.security.core.Authentication;

/**
 * Interface to retrieve authentication details and allows easier testing.
 */
public interface AuthenticationFacade {

  /**
   * Gets the authentication object for this running thread.
   * @return The authentication object for this running thread.
   */
  Authentication getAuthentication();

  /**
   * Sets the authentication object for this running thread.
   * @param authentication The authentication object for this running thread.
   */
  void setAuthentication(Authentication authentication);

}
