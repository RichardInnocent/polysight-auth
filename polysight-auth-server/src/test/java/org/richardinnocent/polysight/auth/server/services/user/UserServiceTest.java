package org.richardinnocent.polysight.auth.server.services.user;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.richardinnocent.polysight.auth.server.models.user.AccountStatus;
import org.richardinnocent.polysight.auth.server.models.user.PolysightUser;
import org.richardinnocent.polysight.auth.server.models.user.RawPolysightUser;
import org.richardinnocent.polysight.auth.server.models.user.UserRole;
import org.richardinnocent.polysight.auth.server.models.user.UserRoleAssignment;
import org.richardinnocent.polysight.auth.server.persistence.exception.NotFoundException;
import org.richardinnocent.polysight.auth.server.persistence.user.PolysightUserDAO;

import java.util.Optional;
import org.richardinnocent.polysight.auth.server.persistence.user.UserRoleAssignmentDAO;
import org.richardinnocent.polysight.auth.server.services.user.creation.UserAlreadyExistsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

  private final PolysightUserDAO userDao = mock(PolysightUserDAO.class);
  private final UserRoleAssignmentDAO userRoleAssignmentDAO = mock(UserRoleAssignmentDAO.class);
  private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
  private final StringKeyGenerator saltGenerator = mock(StringKeyGenerator.class);
  private final UserService service =
      new UserService(userDao, userRoleAssignmentDAO, passwordEncoder, saltGenerator);

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

    UserRoleAssignment role1 = new UserRoleAssignment();
    role1.setUserRole(UserRole.USER);
    UserRoleAssignment role2 = new UserRoleAssignment();
    role2.setUserRole(UserRole.POLYSIGHT_ADMIN);

    List<UserRoleAssignment> roles = Arrays.asList(role1, role2);
    when(userDao.findByEmail(email)).thenReturn(Optional.of(user));
    when(userRoleAssignmentDAO.findAllRolesForUser(user)).thenReturn(roles);

    UserDetails userDetails = service.loadUserByUsername(user.getEmail());
    assertEquals(user.getEmail(), userDetails.getUsername());
    assertEquals(user.getPassword(), userDetails.getPassword());
    assertEquals(roles.stream()
                      .map(UserRoleAssignment::getUserRole)
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

  @Test
  public void testCreateUser() {
    RawPolysightUser rawUser = new RawPolysightUser();
    rawUser.setFirstName("First name");
    rawUser.setLastName("Last name");
    rawUser.setEmail("test@polysight.org");
    rawUser.setDateOfBirth(new LocalDate("2000-07-13"));
    rawUser.setPassword("some password");

    when(service.findByEmail(rawUser.getEmail())).thenReturn(Optional.empty());

    String salt = "generated salt";
    String encryptedPassword = "encrypted password";
    when(saltGenerator.generateKey()).thenReturn(salt);
    when(passwordEncoder.encode(any(CharSequence.class))).thenReturn(encryptedPassword);

    PolysightUser savedUser = service.createUser(rawUser);
    verify(saltGenerator, times(1)).generateKey();
    verify(passwordEncoder, times(1)).encode(rawUser.getPassword() + salt);
    verify(userDao, times(1)).save(savedUser);

    assertEquals(rawUser.getFirstName(), savedUser.getFirstName());
    assertEquals(rawUser.getLastName(), savedUser.getLastName());
    assertEquals(rawUser.getEmail(), savedUser.getEmail());
    assertEquals(rawUser.getDateOfBirth(), savedUser.getDateOfBirth());
    assertEquals(encryptedPassword, savedUser.getPassword());
    assertEquals(salt, savedUser.getPasswordSalt());
    assertTrue(savedUser.getCreationTime().isBeforeNow());
    assertEquals(AccountStatus.ACTIVE, savedUser.getAccountStatus());

    ArgumentCaptor<UserRoleAssignment> roleAssignmentCaptor =
        ArgumentCaptor.forClass(UserRoleAssignment.class);
    verify(userRoleAssignmentDAO).save(roleAssignmentCaptor.capture());

    UserRoleAssignment assignedRole = roleAssignmentCaptor.getValue();
    assertEquals(savedUser.getId(), assignedRole.getUserId());
    assertEquals(UserRole.USER, assignedRole.getUserRole());
  }

  @Test(expected = UserAlreadyExistsException.class)
  public void testCreateUserThrowsExceptionIfUserAlreadyExists() {
    String email = "alreadyregister@polysight.com";
    when(service.findByEmail(email)).thenReturn(Optional.of(mock(PolysightUser.class)));
    RawPolysightUser user = new RawPolysightUser();
    user.setEmail(email);
    service.createUser(user);
  }

  @Test
  public void testDeleteUser() {
    PolysightUser user = mock(PolysightUser.class);
    service.deleteUser(user);
    verify(userDao, times(1)).delete(user);
  }

  @Test
  public void testDeleteUserById() {
    long id = 123L;
    PolysightUser user = mock(PolysightUser.class);
    when(userDao.findById(id)).thenReturn(Optional.of(user));
    service.deleteUser(id);
    verify(userDao, times(1)).delete(user);
  }

  @Test(expected = NotFoundException.class)
  public void testDeleteUserByIdWhenNotFound() {
    when(userDao.findById(anyLong())).thenReturn(Optional.empty());
    service.deleteUser(123L);
  }

}