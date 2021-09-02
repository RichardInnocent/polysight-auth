package org.richardinnocent.polysight.auth.server.http.controller.api.v1.users;

import static org.junit.jupiter.api.Assertions.*;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class OAuth2ResultTest {

  @Test
  public void equalsAndHashCode_Always_Valid() {
    EqualsVerifier.forClass(OAuth2Result.class).suppress(Warning.STRICT_INHERITANCE).verify();
  }

  @Test
  public void forJwt_ParametersSpecified_ResultCreatedAsExpected() {
    String token = "a.b.c.";
    int durationValid = 105;
    OAuth2Result result = OAuth2Result.forJwt(token, durationValid);
    assertEquals(token, result.getAccessToken());
    assertEquals(durationValid, result.getDurationValidSeconds());
    assertEquals("jwt", result.getTokenType());
  }

}