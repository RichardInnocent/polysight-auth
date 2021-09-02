package org.richardinnocent.polysight.auth.server.services.user.auth;

import org.richardinnocent.polysight.auth.server.models.user.PolysightUser;

/**
 * Responsible for building an authentication token for a given user.
 */
public interface TokenBuilder {

  /**
   * Builds an authentication token for a given user.
   * @param user The owner of the token.
   * @return The token.
   */
  Token buildTokenForUser(PolysightUser user);

}
