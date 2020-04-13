package org.richardinnocent.persistence.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception type that implies that the object that should be operated on cannot be found.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NotFoundException extends PersistenceException {

  private NotFoundException(String message) {
    super(message);
  }

  /**
   * Creates a new {@code NotFoundException}, implying that the object that should be operated on
   * could not be found.
   * @param entityType The type of entity.
   * @param id The ID of the entity.
   * @return An appropriate exception.
   */
  public static NotFoundException forEntityTypeWithId(Class<?> entityType, long id) {
    return new NotFoundException(
        "Could not find an entity of type " + entityType.getSimpleName() + " with ID " + id);
  }

}
