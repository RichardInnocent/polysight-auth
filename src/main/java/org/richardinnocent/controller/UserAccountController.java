package org.richardinnocent.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserAccountController {

  @PostMapping
  public void createAccount() {
    throw new NotImplementedException();
  }

  @GetMapping
  public void getAccount() {
    throw new NotImplementedException();
  }

  @PostMapping("/login")
  public void login() {
    throw new NotImplementedException();
  }

  @PostMapping("/validate")
  public void validate() {
    throw new NotImplementedException();
  }

  @ResponseStatus(code = HttpStatus.NOT_IMPLEMENTED)
  private static class NotImplementedException extends RuntimeException {
  }

}
