package org.richardinnocent.polysight.auth.client.request;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.richardinnocent.polysight.auth.client.service.PolysightAuthService;
import org.richardinnocent.polysight.auth.v0_0_3_alpha.model.security.ECPublicKeyResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class PolysightAuthPublicKeyRequestTest {

  @Test
  public void testExecuteInternal() {
    RestTemplateBuilder templateBuilder = mock(RestTemplateBuilder.class);
    RestTemplate template = mock(RestTemplate.class);
    when(templateBuilder.build()).thenReturn(template);

    PolysightAuthService service = mock(PolysightAuthService.class);
    when(service.getVersionedUri()).thenReturn("http://auth-test.com/v1/api");

    @SuppressWarnings("unchecked")
    ResponseEntity<ECPublicKeyResponse> expectedResponse = mock(ResponseEntity.class);

    when(
        template.getForEntity(
            eq(service.getVersionedUri() + "/publickey"), eq(ECPublicKeyResponse.class)
        )
    ).thenReturn(expectedResponse);

    assertEquals(
        expectedResponse,  new PolysightAuthPublicKeyRequest(service, templateBuilder).execute());
  }

}