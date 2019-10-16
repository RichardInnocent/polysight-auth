package org.richardinnocent.persistence.exception;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class InsertionExceptionTest {

  @Test
  public void testNoMessageOrCauseIsSuppliedWhenUsingNoArgConstructor() {
    InsertionException exception = new InsertionException();
    assertNull(exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  public void testMessageIsSetWhenUsingMessageOnlyConstructor() {
    String message = "message";
    InsertionException exception = new InsertionException(message);
    assertEquals(message, exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  public void testCauseIsSetWhenUsingCauseOnlyConstructor() {
    Throwable cause = mock(Throwable.class);
    InsertionException exception = new InsertionException(cause);
    assertEquals(cause, exception.getCause());
  }

  @Test
  public void testMessageAndCauseAreSetWhenUsingMessageAndCauseConstructor() {
    String message = "message";
    Throwable cause = mock(Throwable.class);
    InsertionException exception = new InsertionException(message, cause);
    assertEquals(message, exception.getMessage());
    assertEquals(cause, exception.getCause());
  }

  @Test
  public void testAppropriateResponseCodeIsSpecified() {
    ResponseStatus annotation = InsertionException.class.getAnnotation(ResponseStatus.class);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, annotation.code());
  }

}