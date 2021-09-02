package org.richardinnocent.polysight.auth.server.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.richardinnocent.polysight.auth.server.security.JwtFields.*;

import org.junit.jupiter.api.Test;

public class JwtFieldsTest {

  @Test
  public void testParamNames() {
    assertEquals("Authorization", HEADER_NAME);
    assertEquals("polysight", ISSUER);
    assertEquals("id", USER_ID_CLAIM_KEY);
    assertEquals("email", EMAIL_CLAIM_KEY);
    assertEquals("authorities", AUTHORITIES_CLAIM_KEY);
  }

}