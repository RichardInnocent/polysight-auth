package org.richardinnocent.polysight.auth.server.services.user.creation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UserAlreadyExistsExceptionTest {

  @Test
  public void testForEmail() {
    String email = "user@polysight.com";
    Exception exception = UserAlreadyExistsException.forEmail(email);
    assertEquals("A user already exists with email " + email, exception.getMessage());
  }

}