package org.richardinnocent.polysight.auth.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.richardinnocent.polysight.auth.server.PropertiesKey.JWT_SECRET_LOCATION;

import org.junit.jupiter.api.Test;

public class PropertiesKeysTest {

  @Test
  public void testKeys() {
    assertEquals("jwt.key.secret.location", JWT_SECRET_LOCATION);
  }

}