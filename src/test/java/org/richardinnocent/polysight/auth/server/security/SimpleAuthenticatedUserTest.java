package org.richardinnocent.polysight.auth.server.security;

import static org.junit.jupiter.api.Assertions.*;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class SimpleAuthenticatedUserTest {

  @Test
  public void equalsAndHashCode_Always_Valid() {
    EqualsVerifier
        .forClass(SimpleAuthenticatedUser.class)
        .suppress(Warning.STRICT_INHERITANCE)
        .verify();
  }

  @Test
  public void of_IdAndEmailProvided_IdAndEmailSet() {
    long id = 13L;
    String email = "test@polysight.com";
    SimpleAuthenticatedUser user = SimpleAuthenticatedUser.of(id, email);
    assertEquals(id, user.getId());
    assertEquals(email, user.getEmailAddress());
  }

}