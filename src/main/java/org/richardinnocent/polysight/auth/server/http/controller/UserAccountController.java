package org.richardinnocent.polysight.auth.server.http.controller;

import org.richardinnocent.polysight.auth.server.services.user.UserService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(produces = "application/json")
public class UserAccountController {

  private final UserService userService;

  public UserAccountController(UserService userService) {
    this.userService = userService;
  }

}
