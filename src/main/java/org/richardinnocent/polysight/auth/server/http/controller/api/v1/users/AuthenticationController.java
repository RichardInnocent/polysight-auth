package org.richardinnocent.polysight.auth.server.http.controller.api.v1.users;

import org.richardinnocent.polysight.auth.server.models.user.PolysightUser;
import org.richardinnocent.polysight.auth.server.services.user.auth.Token;
import org.richardinnocent.polysight.auth.server.services.user.auth.TokenBuilder;
import org.richardinnocent.polysight.auth.server.services.user.auth.UserAuthenticationService;
import org.richardinnocent.polysight.auth.server.services.user.auth.UserCredentialsDto;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

  private final UserAuthenticationService userAuthenticationService;
  private final TokenBuilder tokenBuilder;

  public AuthenticationController(
      UserAuthenticationService userAuthenticationService,
      TokenBuilder tokenBuilder
  ) {
    this.userAuthenticationService = userAuthenticationService;
    this.tokenBuilder = tokenBuilder;
  }

  @PostMapping(
      value = "/authenticate",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public OAuth2Result authenticate(@RequestBody UserCredentialsDto userCredentials) {
    PolysightUser user = authenticateAndHandleExceptions(userCredentials);
    Token token = tokenBuilder.buildTokenForUser(user);

    return OAuth2Result.forJwt(token.getToken(), token.getMaxAgeSeconds());
  }

  private PolysightUser authenticateAndHandleExceptions(UserCredentialsDto userCredentials) {
    try {
      return userAuthenticationService.authenticate(userCredentials);
    } catch (AuthenticationException e) {
      throw new BadCredentialsException();
    }
  }

}
