package org.richardinnocent.services.user.find;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.models.user.UserRole;
import org.richardinnocent.persistence.user.PolysightUserDAO;

import java.util.Optional;
import org.richardinnocent.persistence.user.UserRoleAssignmentDAO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserSearchServiceTest {

  private final PolysightUserDAO userDao = mock(PolysightUserDAO.class);
  private final UserRoleAssignmentDAO userRoleAssignmentDAO = mock(UserRoleAssignmentDAO.class);
  private final UserSearchService service = new UserSearchService(userDao, userRoleAssignmentDAO);

  @Test
  public void testFindById() {
    Optional<PolysightUser> result = Optional.of(mock(PolysightUser.class));
    long id = 123L;
    when(userDao.findById(id)).thenReturn(result);
    assertEquals(result, service.findById(id));
  }

  @Test
  public void testFindByEmail() {
    String email = "test@polysight.com";
    Optional<PolysightUser> result = Optional.of(mock(PolysightUser.class));
    when(userDao.findByEmail(email)).thenReturn(result);
    assertEquals(result, service.findByEmail(email));
  }

  @Test
  public void testLoadByUsernameWhenAccountFound() {
    PolysightUser user = mock(PolysightUser.class);
    String email = "test@polysight.com";
    when(user.getEmail()).thenReturn(email);
    when(user.getPassword()).thenReturn("password01");

    List<UserRole> roles = Arrays.asList(UserRole.USER, UserRole.POLYSIGHT_ADMIN);
    when(userDao.findByEmail(email)).thenReturn(Optional.of(user));
    when(userRoleAssignmentDAO.findAllRolesForUser(user)).thenReturn(roles);

    UserDetails userDetails = service.loadUserByUsername(user.getEmail());
    assertEquals(user.getEmail(), userDetails.getUsername());
    assertEquals(user.getPassword(), userDetails.getPassword());
    assertEquals(roles.stream()
                      .map(UserRole::name)
                      .collect(Collectors.toSet()),
                 userDetails.getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toSet()));
  }

  @Test(expected = UsernameNotFoundException.class)
  public void testLoadByUsernameWhenAccountNotFoundThrowsException() {
    when(userDao.findByEmail(any())).thenReturn(Optional.empty());
    service.loadUserByUsername("An unassociated email address");
  }

}