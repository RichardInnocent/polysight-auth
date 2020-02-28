package org.richardinnocent.services.user.creation;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserCreationExceptionTest {

  @Test
  public void testMessageConstructor() {
    String message = "Test exception message";
    assertEquals(message, new UserCreationException(message).getMessage());
  }

}