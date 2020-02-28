package org.richardinnocent.services.user.creation;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserAlreadyExistsExceptionTest {

  @Test
  public void testForEmail() {
    String email = "user@polysight.com";
    Exception exception = UserAlreadyExistsException.forEmail(email);
    assertEquals("A user already exists with email " + email, exception.getMessage());
  }

}