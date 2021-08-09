package org.richardinnocent.polysight.auth.server.services.user.creation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UserCreationExceptionTest {

  @Test
  public void testMessageConstructor() {
    String message = "Test exception message";
    assertEquals(message, new UserCreationException(message).getMessage());
  }

}