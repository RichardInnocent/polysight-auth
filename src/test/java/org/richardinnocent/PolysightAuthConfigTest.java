package org.richardinnocent;

import org.junit.Test;

import static org.junit.Assert.*;

public class PolysightAuthConfigTest {

  private final PolysightAuthConfig config = new PolysightAuthConfig();

  @Test
  public void testPasswordEncoder() {
    // Ensure encoding method hasn't changed
    assertTrue(
        config.passwordEncoder()
            .matches("password",
                "$2a$10$NNg6Gstc838Ylto9QrElb.FKTw4tM1l2vjDo/MGqCz5GhdsNvGawW"));
  }

  @Test
  public void testSaleGeneratorIsNotNull() {
    assertEquals(16, config.saltGenerator().generateKey().length());
  }

}