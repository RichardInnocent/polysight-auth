package org.richardinnocent.persistence.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception type that implies there is a problem retrieving an entity from the database. Note
 * that this is not the same as not being able to find the entity, which should be covered by
 * {@link NotFoundException}.
 */
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class ReadException extends PersistenceException {

  /**
   * Creates an exception type that implies there is a problem retrieving an entity from the
   * database.
   */
  public ReadException() {
    super();
  }

  /**
   * Creates an exception type that implies there is a problem retrieving an entity from the
   * database.
   * @param message The exception message.
   */
  public ReadException(String message) {
    super(message);
  }

  /**
   * Creates an exception type that implies there is a problem retrieving an entity from the
   * database.
   * @param cause The cause.
   */
  public ReadException(Throwable cause) {
    super(cause);
  }

  /**
   * Creates an exception type that implies there is a problem retrieving an entity from the
   * database.
   * @param message The exception message.
   * @param cause The cause.
   */
  public ReadException(String message, Throwable cause) {
    super(message, cause);
  }

}
