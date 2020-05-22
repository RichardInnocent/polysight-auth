package org.richardinnocent.polysight.auth.server.models.user;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.Test;

public class UserRoleTest {

  @Test
  public void ensureUserRolesAreAsExpected() {
    assertEquals(2, UserRole.values().length);
  }

  @Test
  public void testGetAuthority() {
    UserRole role = UserRole.POLYSIGHT_ADMIN;
    assertEquals(role.name(), role.getAuthority().getAuthority());
  }

  @Test
  public void testAllRoleNamesAreShorterThanTheMaxLength() {
    List<String> lengthViolatingRoles =
        Arrays.stream(UserRole.values())
              .map(UserRole::name)
              .filter(name -> name.length() > UserRoleConstraints.USER_ROLE_MAX_LENGTH)
              .collect(Collectors.toList());
    if (lengthViolatingRoles.size() > 0) {
      fail("The following roles have lengths that are too large for the database: "
               + String.join(", ", lengthViolatingRoles));
    }
  }

  @Test
  public void testFromNameReturnsEmptyOptionalForNullName() {
    assertTrue(UserRole.fromName(null).isEmpty());
  }

  @Test
  public void testFromNameReturnsEmptyOptionalIfNameDoesNotMap() {
    assertTrue(UserRole.fromName("not a role").isEmpty());
  }

  @Test
  public void testFromNameReturnsAppropriateRoleWhenNameFound() {
    UserRole userRole = UserRole.POLYSIGHT_ADMIN;
    Optional<UserRole> detectedRole = UserRole.fromName(userRole.name());
    assertTrue(detectedRole.isPresent());
    assertEquals(userRole, detectedRole.get());
  }

  @Test
  public void testFromNameReturnsAppropriateRoleNameWhenNameFoundAndIsCaseInsensitive() {
    UserRole userRole = UserRole.POLYSIGHT_ADMIN;
    Optional<UserRole> detectedRole = UserRole.fromName(userRole.name().toLowerCase());
    assertTrue(detectedRole.isPresent());
    assertEquals(userRole, detectedRole.get());
  }

}