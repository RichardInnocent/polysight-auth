package org.richardinnocent.polysight.auth.server;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class QualifiersTest {

  @Test
  public void testQualifiers() {
    assertEquals("jwt", Qualifiers.JWT);
  }

}