package org.richardinnocent.models.user;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class RawPolysightUserTest {

  @Test
  public void testEqualsContract() {
    EqualsVerifier.forClass(RawPolysightUser.class)
        .suppress(Warning.NONFINAL_FIELDS)
        .suppress(Warning.STRICT_HASHCODE)
        .verify();
  }

}