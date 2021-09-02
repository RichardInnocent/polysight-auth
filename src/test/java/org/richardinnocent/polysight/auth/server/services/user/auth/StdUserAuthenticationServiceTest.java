package org.richardinnocent.polysight.auth.server.services.user.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.richardinnocent.polysight.auth.server.models.user.AccountStatus;
import org.richardinnocent.polysight.auth.server.models.user.PolysightUser;
import org.richardinnocent.polysight.auth.server.services.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

class StdUserAuthenticationServiceTest {

  private static final String USER_EMAIL = "test@polysight.com";

  private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
  private final UserService userService = mock(UserService.class);
  private final UserAuthenticationService authenticationService =
      new StdUserAuthenticationService(authenticationManager, userService);

  private final UserCredentialsDto userCredentials = mock(UserCredentialsDto.class);
  private final PolysightUser user = mock(PolysightUser.class);
  private final Authentication authentication = mock(Authentication.class);

  @BeforeEach
  public void setUp() {
    when(userCredentials.getEmail()).thenReturn(USER_EMAIL);

    when(user.getEmail()).thenReturn(USER_EMAIL);
    when(user.getAccountStatus()).thenReturn(AccountStatus.ACTIVE);
    when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
    when(
        authenticationManager.authenticate(
            argThat(new AuthenticationTokenMatcher(user, userCredentials))
        )
    ).thenReturn(authentication);
    when(authentication.isAuthenticated()).thenReturn(true);
  }

  @Test
  public void authenticate_UserNotFound_ExceptionThrown() {
    when(userCredentials.getEmail()).thenReturn("Not the correct email address");
    try {
      authenticationService.authenticate(userCredentials);
      fail("No exception thrown");
    } catch (BadCredentialsException e) {
      assertEquals("Bad credentials", e.getMessage());
    }
  }

  @Test
  public void authenticate_UserNotActive_ExceptionThrown() {
    when(user.getAccountStatus()).thenReturn(AccountStatus.DISABLED);
    try {
      authenticationService.authenticate(userCredentials);
      fail("No exception thrown");
    } catch (DisabledException e) {
      assertEquals("Your account has been disabled", e.getMessage());
    }
  }

  @Test
  public void authenticate_AuthenticationExceptionThrownByManager_ExceptionThrown() {
    AuthenticationException exception = mock(AuthenticationException.class);
    when(authenticationManager.authenticate(any())).thenThrow(exception);
    try {
      authenticationService.authenticate(userCredentials);
      fail("No exception thrown");
    } catch (AuthenticationException e) {
      assertEquals(exception, e);
    }
  }

  @Test
  public void authenticate_AuthenticationManagerDoesNotAuthenticateUser_ExceptionThrown() {
    when(authentication.isAuthenticated()).thenReturn(false);
    try {
      authenticationService.authenticate(userCredentials);
      fail("No exception thrown");
    } catch (InternalAuthenticationServiceException e) {
      assertEquals(
          "Authentication appeared to complete successfully, but the user was not authenticated",
          e.getMessage()
      );
    }
  }

  @Test
  public void authenticate_UserCredentialsValid_UserReturned() {
    assertEquals(user, authenticationService.authenticate(userCredentials));
  }

  private static class AuthenticationTokenMatcher
      implements ArgumentMatcher<UsernamePasswordAuthenticationToken> {

    private final PolysightUser user;
    private final UserCredentialsDto userCredentials;

    private AuthenticationTokenMatcher(PolysightUser user, UserCredentialsDto userCredentials) {
      this.user = user;
      this.userCredentials = userCredentials;
    }

    @Override
    public boolean matches(UsernamePasswordAuthenticationToken token) {
      return token != null
          && token.getPrincipal() == user
          && token.getCredentials().equals(userCredentials.getPassword() + user.getPasswordSalt());
    }
  }

}