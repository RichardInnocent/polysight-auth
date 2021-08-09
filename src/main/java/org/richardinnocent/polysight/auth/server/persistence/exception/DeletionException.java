package org.richardinnocent.polysight.auth.server.persistence.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception type that implies there was a problem deleting an entity from the database.
 */
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class DeletionException extends PersistenceException {

  /**
   * Creates a new {@code DeletionException}, implying there was a problem deleting an entity from
   * the database.
   */
  public DeletionException() {
    super();
  }

  /**
   * Creates a new {@code DeletionException}, implying there was a problem deleting an entity from
   * the database.
   * @param message The exception message.
   */
  public DeletionException(String message) {
    super(message);
  }

  /**
   * Creates a new {@code DeletionException}, implying there was a problem deleting an entity from
   * the database.
   * @param cause The cause of the exception.
   */
  public DeletionException(Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new {@code DeletionException}, implying there was a problem deleting an entity from
   * the database.
   * @param message The exception message.
   * @param cause The cause of the exception.
   */
  public DeletionException(String message, Throwable cause) {
    super(message, cause);
  }

}
