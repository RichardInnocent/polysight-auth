package org.richardinnocent.persistence.user;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.richardinnocent.models.user.UserRole;
import org.springframework.data.jpa.domain.Specification;

public class UserRoleAssignmentDAOTest {

  private final UserRoleRepository userRoleRepo = mock(UserRoleRepository.class);
  private final UserRoleAssignmentDAO dao = new UserRoleAssignmentDAO(userRoleRepo);

  @Test
  public void testFindAllRolesForUserWithId() {
    List<UserRole> roles = Arrays.asList(UserRole.POLYSIGHT_ADMIN, UserRole.USER);
    when(userRoleRepo.findAll(any(Specification.class))).thenReturn(roles);
    assertEquals(roles, dao.findAllRolesForUserWithId(123L));
  }

  @Test
  public void testFindAllRolesForUser() {
    List<UserRole> roles = Arrays.asList(UserRole.POLYSIGHT_ADMIN, UserRole.USER);
    when(userRoleRepo.findAll(any(Specification.class))).thenReturn(roles);
    assertEquals(roles, dao.findAllRolesForUserWithId(123L));
  }

  @Test(expected = NullPointerException.class)
  public void testFindAllRolesForNullUserThrowsException() {
    dao.findAllRolesForUser(null);
  }

}