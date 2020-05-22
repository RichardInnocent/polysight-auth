package org.richardinnocent.polysight.auth.server.security;

import static org.junit.Assert.assertEquals;
import static org.richardinnocent.polysight.auth.server.security.JWTCookieFields.*;

import org.junit.Test;

public class JWTCookieFieldsTest {

  @Test
  public void testParamNames() {
    assertEquals("polysight-token", COOKIE_NAME);
    assertEquals("polysight", ISSUER);
    assertEquals("email", EMAIL_CLAIM_KEY);
    assertEquals("authorities", AUTHORITIES_CLAIM_KEY);
  }

}