package org.richardinnocent.http.controller;

import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.models.user.RawPolysightUser;
import org.richardinnocent.services.user.creation.UserCreationService;
import org.richardinnocent.services.user.deletion.UserDeletionService;
import org.richardinnocent.services.user.find.UserSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/user", produces = "application/json")
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

  @GetMapping
  public PolysightUser getUser(@RequestParam long id) {
    return searchService.findById(id)
                        .orElse(null);
  }

  @PostMapping(consumes = "application/json")
  @SuppressWarnings("unused")
  public ResponseEntity<PolysightUser> createUser(@Valid @RequestBody RawPolysightUser rawUser) {
    return new ResponseEntity<>(creationService.createUser(rawUser), HttpStatus.CREATED);
  }

  @DeleteMapping
  public PolysightUser deleteAccount(@RequestParam long id) {
    return deletionService.deleteUser(id);
  }

  @PostMapping(value = "/login", consumes = "application/json")
  public void login() {
    throw new NotImplementedException();
  }

  @PostMapping(value = "/validate", consumes = "application/json")
  public void validate() {
    throw new NotImplementedException();
  }

  @ResponseStatus(code = HttpStatus.NOT_IMPLEMENTED)
  private static class NotImplementedException extends RuntimeException {
  }

}
