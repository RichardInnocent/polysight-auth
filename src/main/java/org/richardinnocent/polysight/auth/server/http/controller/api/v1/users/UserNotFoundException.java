package org.richardinnocent.polysight.auth.server.http.controller.api.v1.users;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception to indicate that a user with the given ID cannot be found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

  /**
   * Creates a new exception to indicate that a user with the given ID cannot be found.
   * @param id The ID of the user.
   */
  public UserNotFoundException(long id) {
    super("User not found with ID " + id);
  }

}
