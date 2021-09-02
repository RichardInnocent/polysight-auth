package org.richardinnocent.polysight.auth.server.http.controller.api.v1.users;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.richardinnocent.polysight.auth.server.models.user.AccountStatus;
import org.richardinnocent.polysight.auth.server.models.user.PolysightUser;
import org.richardinnocent.polysight.auth.server.security.SecurityContextAuthenticationFacade;
import org.richardinnocent.polysight.auth.server.security.SimpleAuthenticatedUser;
import org.richardinnocent.polysight.auth.server.services.user.UserService;
import org.springframework.security.core.Authentication;

class UserControllerTest {

  private static final long USER_ID = 12L;
  private static final String FIRST_NAME = "Test";
  private static final String LAST_NAME = "User";
  private static final String EMAIL_ADDRESS = "user@test.com";
  private static final LocalDate DATE_OF_BIRTH = LocalDate.of(2021, 8, 9);
  private static final AccountStatus ACCOUNT_STATUS = AccountStatus.ACTIVE;

  private final SecurityContextAuthenticationFacade authenticationFacade =
      spy(SecurityContextAuthenticationFacade.class);

  private final Authentication authentication = mock(Authentication.class);

  private final UserService userService = mock(UserService.class);

  private final SimpleAuthenticatedUser principal = mock(SimpleAuthenticatedUser.class);

  private final PolysightUser user = mock(PolysightUser.class);

  private final UserController controller = new UserController(authenticationFacade, userService);

  @BeforeEach
  public void setUp() {
    when(authenticationFacade.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(principal);

    when(principal.getId()).thenReturn(USER_ID);
    when(principal.getEmailAddress()).thenReturn(EMAIL_ADDRESS);

    when(user.getId()).thenReturn(USER_ID);
    when(user.getFirstName()).thenReturn(FIRST_NAME);
    when(user.getLastName()).thenReturn(LAST_NAME);
    when(user.getEmail()).thenReturn(EMAIL_ADDRESS);
    when(user.getDateOfBirth()).thenReturn(DATE_OF_BIRTH);
    when(user.getAccountStatus()).thenReturn(ACCOUNT_STATUS);

    when(userService.findById(USER_ID)).thenReturn(Optional.of(user));
  }

  @Test
  public void getUser_AuthenticationNotPresent_ExceptionThrown() {
    when(authenticationFacade.getAuthentication()).thenReturn(null);

    try {
      controller.getUser(USER_ID);
      fail("No exception thrown");
    } catch (UserNotFoundException e) {
      ensureNotFoundExceptionIsCorrectlyFormed(e);
    }
  }

  @Test
  public void getUser_PrincipalNotPresent_ExceptionThrown() {
    when(authentication.getPrincipal()).thenReturn(null);

    try {
      controller.getUser(USER_ID);
      fail("No exception thrown");
    } catch (UserNotFoundException e) {
      ensureNotFoundExceptionIsCorrectlyFormed(e);
    }
  }

  @Test
  public void getUser_PrincipalNotPolysightUser_ExceptionThrown() {
    when(authentication.getPrincipal()).thenReturn("Not the correct principal type");

    try {
      controller.getUser(USER_ID);
      fail("No exception thrown");
    } catch (UserNotFoundException e) {
      ensureNotFoundExceptionIsCorrectlyFormed(e);
    }
  }

  @Test
  public void getUser_IdOfAuthenticatedUserDoesNotMatchRequestedId_ExceptionThrown() {
    when(principal.getId()).thenReturn(USER_ID + 1L);
    try {
      controller.getUser(USER_ID);
      fail("No exception thrown");
    } catch (UserNotFoundException e) {
      ensureNotFoundExceptionIsCorrectlyFormed(e);
    }
  }

  @Test
  public void getUser_IdOfAuthenticatedUserMatchesRequestedId_InformationReturned() {
    UserDto dto = controller.getUser(USER_ID);
    assertEquals(USER_ID, dto.getId());
    assertEquals(FIRST_NAME, dto.getFirstName());
    assertEquals(LAST_NAME, dto.getLastName());
    assertEquals(EMAIL_ADDRESS, dto.getEmail());
    assertEquals(DATE_OF_BIRTH, dto.getDateOfBirth());
    assertEquals(ACCOUNT_STATUS, dto.getAccountStatus());
  }

  private static void ensureNotFoundExceptionIsCorrectlyFormed(UserNotFoundException e) {
    assertEquals("User not found with ID " + USER_ID, e.getMessage());
  }

}