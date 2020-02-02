package org.richardinnocent;

import static org.junit.Assert.*;

import org.junit.Test;

import static org.richardinnocent.PropertiesKey.*;

public class PropertiesKeysTest {

  @Test
  public void testKeys() {
    assertEquals("jwt.key.secret.location", JWT_SECRET_LOCATION);
  }

}