package org.richardinnocent;

import static org.junit.Assert.*;

import org.junit.Test;

public class QualifiersTest {

  @Test
  public void testQualifiers() {
    assertEquals("jwt", Qualifiers.JWT);
  }

}