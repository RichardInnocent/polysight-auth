package org.richardinnocent.http.controller;

import org.richardinnocent.models.user.RawPolysightUser;
import org.richardinnocent.services.user.creation.UserCreationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserAccountController {

  private final UserCreationService userCreationService;

  public UserAccountController(UserCreationService userCreationService) {
    this.userCreationService = userCreationService;
  }

  @PostMapping(consumes = "application/json")
  @SuppressWarnings("unused")
  public ResponseEntity<Void> createAccount(@Valid @RequestBody RawPolysightUser rawUser) {
    userCreationService.createUser(rawUser);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping
  public void getAccount() {
    throw new NotImplementedException();
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
