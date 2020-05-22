package org.richardinnocent.polysight.auth.server.models.user;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

public class AccountStatusTest {

  @Test
  public void testLogOnStatuses() {
    assertTrue(AccountStatus.ACTIVE.isAbleToLogOn());
    assertFalse(AccountStatus.DISABLED.isAbleToLogOn());
  }

  @Test
  public void testAccountStatusesAreAsExpected() {
    assertEquals(2, AccountStatus.values().length);
  }

  @Test
  public void testAllAccountStatusesAreShortEnoughToBeInsertedIntoDatabase() {
    List<String> lengthViolatingStatuses =
        Arrays.stream(AccountStatus.values())
              .map(AccountStatus::name)
              .filter(name -> name.length() > UserRoleConstraints.USER_ROLE_MAX_LENGTH)
              .collect(Collectors.toList());
    if (lengthViolatingStatuses.size() > 0) {
      fail("The following statuses have lengths that are too large for the database: "
               + String.join(", ", lengthViolatingStatuses));
    }
  }

}