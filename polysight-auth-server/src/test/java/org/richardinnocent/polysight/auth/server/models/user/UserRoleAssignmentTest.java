package org.richardinnocent.polysight.auth.server.models.user;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class UserRoleAssignmentTest {

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.forClass(UserRoleAssignment.class)
                  .suppress(Warning.NONFINAL_FIELDS, Warning.STRICT_INHERITANCE)
                  .verify();
  }

}