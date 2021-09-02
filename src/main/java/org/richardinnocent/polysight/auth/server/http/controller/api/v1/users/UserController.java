package org.richardinnocent.polysight.auth.server.http.controller.api.v1.users;

import java.util.Optional;
import org.richardinnocent.polysight.auth.server.models.user.PolysightUser;
import org.richardinnocent.polysight.auth.server.models.user.RawPolysightUser;
import org.richardinnocent.polysight.auth.server.security.SecurityContextAuthenticationFacade;
import org.richardinnocent.polysight.auth.server.security.SimpleAuthenticatedUser;
import org.richardinnocent.polysight.auth.server.services.user.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final SecurityContextAuthenticationFacade authenticationFacade;
  private final UserService userService;

  public UserController(
      SecurityContextAuthenticationFacade authenticationFacade,
      UserService userService
  ) {
    this.authenticationFacade = authenticationFacade;
    this.userService = userService;
  }

  /**
   * Gets the information for the user with the specified ID.
   * @param id The ID of the user.
   * @return The information for the user.
   * @throws UserNotFoundException Thrown if the user cannot be found, or the authenticated user
   * does not have access to the specified user's information.
   */
  @GetMapping(
      value = "/users/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public UserDto getUser(@PathVariable("id") long id) throws UserNotFoundException {
    PolysightUser user = Optional
        .ofNullable(authenticationFacade.getAuthenticatedUser())
        .filter(Optional::isPresent)
        .map(Optional::get)
        .filter(authenticatedUser -> authenticatedUser.getId() == id)
        .map(SimpleAuthenticatedUser::getId)
        .map(userService::findById)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .orElseThrow(() -> new UserNotFoundException(id));

    return UserDto.forUser(user);
  }

  @PostMapping(
      value = "/users",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public UserDto createUser(@RequestBody RawPolysightUser rawPolysightUser) {
    PolysightUser user = userService.createUser(rawPolysightUser);
    return UserDto.forUser(user);
  }

}
