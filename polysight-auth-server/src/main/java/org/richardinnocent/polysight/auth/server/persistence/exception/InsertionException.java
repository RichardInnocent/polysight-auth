package org.richardinnocent.polysight.auth.server.persistence.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception type to imply that  that there is an issue when creating entries in the database.
 */
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class InsertionException extends PersistenceException {

  /**
   * Creates a new {@code InsertionException}, implying that there is an issue when creating
   * entries in the database.
   */
  public InsertionException() {
    super();
  }

  /**
   * Creates a new {@code InsertionException}, implying that there is an issue when creating
   * entries in the database.
   * @param message The exception message.
   */
  public InsertionException(String message) {
    super(message);
  }

  /**
   * Creates a new {@code InsertionException}, implying that there is an issue when creating
   * entries in the database.
   * @param cause The cause of the exception.
   */
  public InsertionException(Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new {@code InsertionException}, implying that there is an issue when creating
   * entries in the database.
   * @param message The exception message.
   * @param cause The cause of the exception.
   */
  public InsertionException(String message, Throwable cause) {
    super(message, cause);
  }

}
