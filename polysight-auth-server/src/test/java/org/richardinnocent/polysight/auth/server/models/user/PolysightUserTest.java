package org.richardinnocent.polysight.auth.server.models.user;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.*;

public class PolysightUserTest {

  @Test
  public void testCreationDateIsAlwaysStoredInUtc() {
    PolysightUser user = new PolysightUser();
    DateTime nonUtcTime = new DateTime("2019-10-13T17:21:03.345+03");
    user.setCreationTime(nonUtcTime);
    assertEquals(nonUtcTime.withZone(DateTimeZone.UTC), user.getCreationTime());
  }

  @Test
  public void testEqualsAndHashCode() {
    EqualsVerifier.forClass(PolysightUser.class)
                  .suppress(Warning.STRICT_INHERITANCE,
                            Warning.STRICT_HASHCODE,
                            Warning.NONFINAL_FIELDS)
                  .verify();
  }

}