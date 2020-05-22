package org.richardinnocent.polysight.auth.client.request;

import org.richardinnocent.polysight.auth.client.service.PolysightAuthService;
import org.richardinnocent.polysight.core.client.request.RestTemplateServiceRequest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class PolysightAuthPingRequest
    extends RestTemplateServiceRequest<PolysightAuthService, Void> {

  public PolysightAuthPingRequest(
      PolysightAuthService service, RestTemplateBuilder templateBuilder) {
    super(service, templateBuilder);
  }

  @Override
  protected ResponseEntity<Void> executeInternal(
      PolysightAuthService service, RestTemplate template) {
    return template.getForEntity(service.getBaseUri() + "api/v1/ping", Void.class);
  }
}
