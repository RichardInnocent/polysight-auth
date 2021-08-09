package org.richardinnocent.polysight.auth.server.models.user;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

public class PolysightUserTest {

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier
        .forClass(PolysightUser.class)
        .suppress(
            Warning.STRICT_INHERITANCE,
            Warning.STRICT_HASHCODE,
            Warning.NONFINAL_FIELDS
        )
        .verify();
  }

}