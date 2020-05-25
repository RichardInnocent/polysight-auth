package org.richardinnocent.polysight.auth.client.request;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.richardinnocent.polysight.auth.client.service.PolysightAuthService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class PolysightAuthPingRequestTest {

  @Test
  public void testInternal() {
    RestTemplateBuilder templateBuilder = mock(RestTemplateBuilder.class);
    RestTemplate template = mock(RestTemplate.class);
    when(templateBuilder.build()).thenReturn(template);

    PolysightAuthService service = mock(PolysightAuthService.class);
    when(service.getVersionedUri()).thenReturn("http://polysight-auth/api/v1");

    @SuppressWarnings("unchecked")
    ResponseEntity<Void> expectedResponse = mock(ResponseEntity.class);

    when(template.getForEntity(eq(service.getVersionedUri() + "/ping"), eq(Void.class)))
        .thenReturn(expectedResponse);

    assertEquals(
        expectedResponse, new PolysightAuthPingRequest(service, templateBuilder).execute());
  }

}