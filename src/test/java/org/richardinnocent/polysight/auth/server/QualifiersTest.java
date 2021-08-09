package org.richardinnocent.polysight.auth.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class QualifiersTest {

  @Test
  public void testQualifiers() {
    assertEquals("jwt", Qualifiers.JWT);
  }

}