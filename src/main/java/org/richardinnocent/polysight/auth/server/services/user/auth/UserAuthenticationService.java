package org.richardinnocent.polysight.auth.server.services.user.auth;

import org.richardinnocent.polysight.auth.server.models.user.PolysightUser;
import org.springframework.security.core.AuthenticationException;

/**
 * Responsible for authenticating a user given their credentials.
 */
public interface UserAuthenticationService {

  /**
   * Returns the appropriate user based on their credentials.
   * @param userCredentials The user's credentials.
   * @return The user associated with the credentials.
   * @throws AuthenticationException Thrown if there is a problem authenticating, such as the user
   * does not exist, or their password does not match.
   */
  PolysightUser authenticate(UserCredentialsDto userCredentials) throws AuthenticationException;

}
