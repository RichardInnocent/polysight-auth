package org.richardinnocent.http.controller;

import java.security.Principal;
import javax.validation.Valid;
import org.joda.time.LocalDate;
import org.richardinnocent.http.binder.joda.LocalDateEditor;
import org.richardinnocent.models.user.RawPolysightUser;
import org.richardinnocent.services.user.UserService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


@Controller
@RequestMapping(produces = "application/json")
public class UserAccountController {

  private final UserService userService;

  public UserAccountController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("signup")
  @SuppressWarnings("unused")
  public String signUp(Model model) {
    model.addAttribute("user", new RawPolysightUser());
    return "signup";
  }

  @PostMapping(value = "signup", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  @SuppressWarnings("unused")
  public RedirectView signUp(@Valid @ModelAttribute RawPolysightUser rawPolysightUser) {
    userService.createUser(rawPolysightUser);
    return new RedirectView("/login");
  }

  @GetMapping("profile")
  @SuppressWarnings("unused")
  public String profile(Model model) {
    return "profile";
  }

  @DeleteMapping("user")
  public String deleteUser(@RequestParam String email, Principal principal) {
    throw new UnsupportedOperationException();
  }

  @InitBinder
  public void dataBinding(WebDataBinder binder) {
    binder.registerCustomEditor(LocalDate.class, new LocalDateEditor());
  }

}
