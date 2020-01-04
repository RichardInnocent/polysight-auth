package org.richardinnocent.http.controller;

import org.richardinnocent.services.user.creation.UserCreationService;
import org.richardinnocent.services.user.deletion.UserDeletionService;
import org.richardinnocent.services.user.find.UserSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(produces = "application/json")
public class UserAccountController {

  private final UserSearchService searchService;
  private final UserCreationService creationService;
  private final UserDeletionService deletionService;

  public UserAccountController(UserSearchService searchService,
                               UserCreationService creationService,
                               UserDeletionService deletionService) {
    this.searchService = searchService;
    this.creationService = creationService;
    this.deletionService = deletionService;
  }

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @ResponseStatus(code = HttpStatus.NOT_IMPLEMENTED)
  private static class NotImplementedException extends RuntimeException {
  }

}
