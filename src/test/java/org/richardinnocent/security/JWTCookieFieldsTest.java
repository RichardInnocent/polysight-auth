package org.richardinnocent.security;

import static org.junit.Assert.*;
import static org.richardinnocent.security.JWTCookieFields.*;

import org.junit.Test;

public class JWTCookieFieldsTest {

  @Test
  public void testParamNames() {
    assertEquals("polysight-token", COOKIE_NAME);
    assertEquals("polysight", ISSUER);
    assertEquals("email", EMAIL_CLAIM_KEY);
  }

}