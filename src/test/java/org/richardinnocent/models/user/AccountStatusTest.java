package org.richardinnocent.models.user;

import static org.junit.Assert.*;

import org.junit.Test;

public class AccountStatusTest {

  @Test
  public void testLogOnStatuses() {
    assertTrue(AccountStatus.ACTIVE.isAbleToLogOn());
    assertFalse(AccountStatus.DISABLED.isAbleToLogOn());
  }

}