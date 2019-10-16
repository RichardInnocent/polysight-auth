package org.richardinnocent.persistence.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception type to imply that there is a problem when executing a statement against the database.
 */
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class PersistenceException extends RuntimeException {

  /**
   * Creates a new {@code PersistenceException}, implying that there is a problem encountered when
   * executing a statement against the database.
   */
  public PersistenceException() {
    super();
  }

  /**
   * Creates a new {@code PersistenceException}, implying that there is a problem encountered when
   * executing a statement against the database.
   * @param message The exception message.
   */
  public PersistenceException(String message) {
    super(message);
  }

  /**
   * Creates a new {@code PersistenceException}, implying that there is a problem encountered when
   * executing a statement against the database.
   * @param cause The cause of the exception.
   */
  public PersistenceException(Throwable cause) {
    super(cause);
  }

  /**
   * Creates a new {@code PersistenceException}, implying that there is a problem encountered when
   * executing a statement against the database.
   * @param message The exception message.
   * @param cause The cause of the exception.
   */
  public PersistenceException(String message, Throwable cause) {
    super(message, cause);
  }

}
