package org.richardinnocent.polysight.auth.server.http.controller.api.v1.users;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserNotFoundExceptionTest {

  @Test
  public void constructor_IdSpecified_ExceptionMessageSet() {
    long id = 5L;
    assertEquals("User not found with ID " + id, new UserNotFoundException(id).getMessage());
  }

}