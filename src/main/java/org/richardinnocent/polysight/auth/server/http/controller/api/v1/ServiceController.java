package org.richardinnocent.polysight.auth.server.http.controller.api.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to return specific information about the server.
 */
@RestController
@RequestMapping("/api/v1")
public class ServiceController {

  /**
   * Pings the server to check that the application is still working correctly.
   */
  @GetMapping("/ping")
  @SuppressWarnings("unused")
  public void ping() {}

}
