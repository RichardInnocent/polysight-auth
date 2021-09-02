package org.richardinnocent.polysight.auth.server.services.user.auth;

import static org.junit.jupiter.api.Assertions.*;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class TokenTest {

  private static final String TOKEN_STRING = "Test token";
  private static final int MAX_AGE_SECONDS = 3600;

  @Test
  public void equalsAndHashCode_Always_Valid() {
    EqualsVerifier.forClass(Token.class).suppress(Warning.STRICT_INHERITANCE).verify();
  }

  @Test
  public void constructor$StringInt_ValidParameters_ParametersSet() {
    Token token = new Token(TOKEN_STRING, MAX_AGE_SECONDS);
    assertEquals(TOKEN_STRING, token.getToken());
    assertEquals(MAX_AGE_SECONDS, token.getMaxAgeSeconds());
  }

  @Test
  public void constructor$StringInt_TokenIsNull_ExceptionThrown() {
    try {
      new Token(null, MAX_AGE_SECONDS);
      fail("No exception thrown");
    } catch (NullPointerException e) {
      assertEquals("Token is null", e.getMessage());
    }
  }

  @Test
  public void constructor_StringInt_MaxAgeIsNegative_ExceptionThrown() {
    int maxAge = -1;
    try {
      new Token(TOKEN_STRING, maxAge);
      fail("No exception thrown");
    } catch (IllegalArgumentException e) {
      assertEquals("Max age of the token cannot be < 0 but was " + maxAge, e.getMessage());
    }
  }

}