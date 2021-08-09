package org.richardinnocent.polysight.auth.server.http.controller.api.v1;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.richardinnocent.polysight.auth.server.http.controller.ControllerEndpointTest;

public class ServiceControllerTest extends ControllerEndpointTest {

  private final ServiceController controller = new ServiceController();

  @Test
  public void testPing() throws Exception {
    mvc.perform(get("/api/v1/ping")).andExpect(status().isOk());
  }

  @Override
  protected Object getController() {
    return controller;
  }
}