package org.richardinnocent.polysight.auth.server.services.user.auth;

import java.util.Collections;
import java.util.Objects;
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
import org.springframework.stereotype.Component;

@Component
public class StdUserAuthenticationService implements UserAuthenticationService {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;

  public StdUserAuthenticationService(
      AuthenticationManager authenticationManager,
      UserService userService
  ) throws NullPointerException {
    this.authenticationManager =
        Objects.requireNonNull(authenticationManager, "Authentication manager is null");
    this.userService = Objects.requireNonNull(userService, "User service is null");
  }

  @Override
  public PolysightUser authenticate(UserCredentialsDto userCredentials)
      throws AuthenticationException {
    PolysightUser user = userService
        .findByEmail(userCredentials.getEmail())
        .orElseThrow(() -> new BadCredentialsException("Bad credentials"));

    if (user.getAccountStatus() != AccountStatus.ACTIVE) {
      throw new DisabledException("Your account has been disabled");
    }

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            user,
            userCredentials.getPassword() + user.getPasswordSalt(),
            Collections.emptyList()
        )
    );

    if (authentication.isAuthenticated()) {
      return user;
    }

    throw new InternalAuthenticationServiceException(
        "Authentication appeared to complete successfully, but the user was not authenticated"
    );
  }
}
