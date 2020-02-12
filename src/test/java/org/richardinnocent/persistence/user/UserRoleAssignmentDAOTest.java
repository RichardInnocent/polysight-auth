package org.richardinnocent.persistence.user;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.richardinnocent.models.user.UserRoleAssignment;
import org.springframework.data.jpa.domain.Specification;

public class UserRoleAssignmentDAOTest {

  private final UserRoleAssignmentRepository userRoleRepo = mock(UserRoleAssignmentRepository.class);
  private final UserRoleAssignmentDAO dao = new UserRoleAssignmentDAO(userRoleRepo);

  @Test
  @SuppressWarnings("unchecked")
  public void testFindAllRolesForUserWithId() {
    List<UserRoleAssignment> roles =
        Arrays.asList(mock(UserRoleAssignment.class), mock(UserRoleAssignment.class));
    when(userRoleRepo.findAll(any(Specification.class))).thenReturn(roles);
    assertEquals(roles, dao.findAllRolesForUserWithId(123L));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testFindAllRolesForUser() {
    List<UserRoleAssignment> roles =
        Arrays.asList(mock(UserRoleAssignment.class), mock(UserRoleAssignment.class));
    when(userRoleRepo.findAll(any(Specification.class))).thenReturn(roles);
    assertEquals(roles, dao.findAllRolesForUserWithId(123L));
  }

  @Test(expected = NullPointerException.class)
  public void testFindAllRolesForNullUserThrowsException() {
    dao.findAllRolesForUser(null);
  }

}