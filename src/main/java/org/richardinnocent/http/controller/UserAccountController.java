package org.richardinnocent.http.controller;

import javax.validation.Valid;
import org.joda.time.LocalDate;
import org.richardinnocent.http.binder.joda.LocalDateEditor;
import org.richardinnocent.models.user.RawPolysightUser;
import org.richardinnocent.services.user.creation.UserCreationService;
import org.richardinnocent.services.user.deletion.UserDeletionService;
import org.richardinnocent.services.user.find.UserSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
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

  @GetMapping("signup")
  @SuppressWarnings("unused")
  public String signUp(Model model) {
    model.addAttribute("user", new RawPolysightUser());
    return "signup";
  }

  @PostMapping(value = "signup", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  @SuppressWarnings("unused")
  public String signUp(@Valid @ModelAttribute RawPolysightUser rawPolysightUser) {
    creationService.createUser(rawPolysightUser);
    return "login";
  }

  @GetMapping("profile")
  public String profile(Model model) {
    return "profile";
  }

  @ResponseStatus(code = HttpStatus.NOT_IMPLEMENTED)
  private static class NotImplementedException extends RuntimeException {
  }

  @InitBinder
  public void dataBinding(WebDataBinder binder) {
    binder.registerCustomEditor(LocalDate.class, new LocalDateEditor());
  }

}
