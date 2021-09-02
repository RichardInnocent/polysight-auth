package org.richardinnocent.polysight.auth.server.services.user.creation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Specifies that a user cannot be created because a user with a unique key that would be duplicated
 * by this user already exists.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends UserCreationException {

  private UserAlreadyExistsException(String message) {
    super(message);
  }

  /**
   * Creates an exception to specify that a user cannot be created as a user already exists with the
   * given email.
   * @param email The email address.
   * @return A new exception.
   */
  public static UserAlreadyExistsException forEmail(String email) {
    return new UserAlreadyExistsException("A user already exists with email " + email);
  }
}
