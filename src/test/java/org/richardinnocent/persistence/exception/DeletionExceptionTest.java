package org.richardinnocent.persistence.exception;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class DeletionExceptionTest {

  @Test
  public void testNoMessageOrCauseIsSuppliedWhenUsingNoArgConstructor() {
    DeletionException exception = new DeletionException();
    assertNull(exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  public void testMessageIsSetWhenUsingMessageOnlyConstructor() {
    String message = "message";
    DeletionException exception = new DeletionException(message);
    assertEquals(message, exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  public void testCauseIsSetWhenUsingCauseOnlyConstructor() {
    Throwable cause = mock(Throwable.class);
    DeletionException exception = new DeletionException(cause);
    assertEquals(cause, exception.getCause());
  }

  @Test
  public void testMessageAndCauseAreSetWhenUsingMessageAndCauseConstructor() {
    String message = "message";
    Throwable cause = mock(Throwable.class);
    DeletionException exception = new DeletionException(message, cause);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }

  @Test
  public void testAppropriateResponseCodeIsSpecified() {
    ResponseStatus annotation = DeletionException.class.getAnnotation(ResponseStatus.class);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, annotation.code());
  }

}