package org.richardinnocent.polysight.auth.server;

import static org.junit.Assert.assertEquals;
import static org.richardinnocent.polysight.auth.server.PropertiesKey.JWT_SECRET_LOCATION;

import org.junit.Test;

public class PropertiesKeysTest {

  @Test
  public void testKeys() {
    assertEquals("jwt.key.secret.location", JWT_SECRET_LOCATION);
  }

}