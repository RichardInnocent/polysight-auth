package org.richardinnocent.persistence.exception;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.Assert.*;

public class NotFoundExceptionTest {

  @Test
  public void testForEntityTypeWithId() {
    Class entityType = NotFoundExceptionTest.class;
    long id = 123L;
    NotFoundException exception = NotFoundException.forEntityTypeWithId(entityType, id);
    assertEquals(
        "Could not find an entity of type " + entityType.getSimpleName() + " with ID " + id,
        exception.getMessage());
  }

  @Test
  public void testResponseCodeIsBadRequest() {
    ResponseStatus responseStatus = NotFoundException.class.getAnnotation(ResponseStatus.class);
    assertEquals(HttpStatus.BAD_REQUEST, responseStatus.code());
  }

}