package org.richardinnocent.polysight.auth.server.services.user.auth;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class UserCredentialsDtoTest {

  @Test
  public void equalsAndHashCode_Always_Valid() {
    EqualsVerifier
        .forClass(UserCredentialsDto.class)
        .suppress(Warning.NONFINAL_FIELDS, Warning.STRICT_INHERITANCE)
        .verify();
  }

}