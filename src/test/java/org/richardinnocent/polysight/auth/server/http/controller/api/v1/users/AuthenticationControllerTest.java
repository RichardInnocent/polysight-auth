package org.richardinnocent.polysight.auth.server.http.controller.api.v1.users;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.richardinnocent.polysight.auth.server.models.user.PolysightUser;
import org.richardinnocent.polysight.auth.server.services.user.auth.Token;
import org.richardinnocent.polysight.auth.server.services.user.auth.TokenBuilder;
import org.richardinnocent.polysight.auth.server.services.user.auth.UserAuthenticationService;
import org.richardinnocent.polysight.auth.server.services.user.auth.UserCredentialsDto;
import org.springframework.security.core.AuthenticationException;

class AuthenticationControllerTest {

  private static final String EXPECTED_TOKEN = "Expected token";
  private static final int MAX_TOKEN_AGE = 3600;

  private final UserAuthenticationService userAuthenticationService =
      mock(UserAuthenticationService.class);
  private final TokenBuilder tokenBuilder = mock(TokenBuilder.class);
  private final AuthenticationController controller =
      new AuthenticationController(userAuthenticationService, tokenBuilder);
  private final UserCredentialsDto userCredentials = mock(UserCredentialsDto.class);
  private final PolysightUser user = mock(PolysightUser.class);
  private final Token token = mock(Token.class);

  @BeforeEach
  public void setUp() {
    when(userAuthenticationService.authenticate(userCredentials)).thenReturn(user);
    when(tokenBuilder.buildTokenForUser(user)).thenReturn(token);
    when(token.getToken()).thenReturn(EXPECTED_TOKEN);
    when(token.getMaxAgeSeconds()).thenReturn(MAX_TOKEN_AGE);
  }

  @Test
  public void authenticate_BadCredentials_ExceptionThrown() {
    when(userAuthenticationService.authenticate(userCredentials))
        .thenThrow(mock(AuthenticationException.class));
    assertThrows(
        BadCredentialsException.class,
        () -> controller.authenticate(userCredentials)
    );
  }

  @Test
  public void authenticate_CredentialsValid_JwtCreated() {
    OAuth2Result result = controller.authenticate(userCredentials);
    assertEquals(EXPECTED_TOKEN, result.getAccessToken());
    assertEquals(MAX_TOKEN_AGE, result.getDurationValidSeconds());
    assertEquals("jwt", result.getTokenType());
  }

}