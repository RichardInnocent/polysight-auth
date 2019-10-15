package org.richardinnocent.models.user;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.*;

public class PolysightUserTest {

  private PolysightUser user = new PolysightUser();

  @Test
  public void testCreationDateIsAlwaysStoredInUtc() {
    DateTime nonUtcTime = new DateTime("2019-10-13T17:21:03.345+03");
    user.setCreationTime(nonUtcTime);
    assertEquals(nonUtcTime.withZone(DateTimeZone.UTC), user.getCreationTime());
  }

}