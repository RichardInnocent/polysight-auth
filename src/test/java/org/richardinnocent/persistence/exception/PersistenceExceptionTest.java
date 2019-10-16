package org.richardinnocent.persistence.exception;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class PersistenceExceptionTest {

  @Test
  public void testNoMessageOrCauseIsSuppliedWhenUsingNoArgConstructor() {
    PersistenceException exception = new PersistenceException();
    assertNull(exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  public void testMessageIsSetWhenUsingMessageOnlyConstructor() {
    String message = "message";
    PersistenceException exception = new PersistenceException(message);
    assertEquals(message, exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  public void testCauseIsSetWhenUsingCauseOnlyConstructor() {
    Throwable cause = mock(Throwable.class);
    PersistenceException exception = new PersistenceException(cause);
    assertEquals(cause, exception.getCause());
  }

  @Test
  public void testMessageAndCauseAreSetWhenUsingMessageAndCauseConstructor() {
    String message = "message";
    Throwable cause = mock(Throwable.class);
    PersistenceException exception = new PersistenceException(message, cause);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }

  @Test
  public void testAppropriateResponseCodeIsSpecified() {
    ResponseStatus annotation = PersistenceException.class.getAnnotation(ResponseStatus.class);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, annotation.code());
  }

}