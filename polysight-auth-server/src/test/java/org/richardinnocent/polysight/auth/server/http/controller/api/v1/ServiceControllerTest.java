package org.richardinnocent.polysight.auth.server.http.controller.api.v1;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.richardinnocent.polysight.auth.server.http.controller.ControllerEndpointTest;
import org.richardinnocent.polysight.auth.server.security.PublicPrivateKeyProvider;
import org.springframework.http.MediaType;

public class ServiceControllerTest extends ControllerEndpointTest {

  private final PublicPrivateKeyProvider keyProvider = mock(PublicPrivateKeyProvider.class);
  private final ServiceController controller = new ServiceController(keyProvider);

  @Test
  public void testPing() throws Exception {
    mvc.perform(get("/api/v1/ping"))
       .andExpect(status().isOk());
  }

  @Test
  public void testPublicKey() throws Exception {
    PublicKey publicKey = mock(PublicKey.class);
    String algorithm = "test-algorithm";
    String format = "test-format";
    String key = "test-public-key";
    when(publicKey.getAlgorithm()).thenReturn(algorithm);
    when(publicKey.getFormat()).thenReturn(format);
    when(publicKey.getEncoded()).thenReturn(key.getBytes());
    when(keyProvider.getPublicKey()).thenReturn(publicKey);

    Map<String, Object> expectedContent = new HashMap<>(3);
    expectedContent.put("algorithm", algorithm);
    expectedContent.put("format", format);
    expectedContent.put("key", key.getBytes());
    String expectedContentJson = new ObjectMapper().writeValueAsString(expectedContent);

    mvc.perform(get("/api/v1/publickey"))
       .andExpect(status().isOk())
       .andExpect(content().contentType(MediaType.APPLICATION_JSON))
       .andExpect(content().json(expectedContentJson));
  }

  @Override
  protected Object getController() {
    return controller;
  }
}