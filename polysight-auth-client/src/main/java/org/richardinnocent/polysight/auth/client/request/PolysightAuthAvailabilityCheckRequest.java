package org.richardinnocent.polysight.auth.client.request;

import org.richardinnocent.polysight.auth.client.service.PolysightAuthService;
import org.richardinnocent.polysight.core.client.request.RestTemplateServiceRequest;
import org.richardinnocent.polysight.core.client.service.MajorOnlyServiceVersion;
import org.richardinnocent.polysight.core.client.service.PolysightServiceConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * A request to check whether the authentication service is available.
 */
public class PolysightAuthAvailabilityCheckRequest
    extends RestTemplateServiceRequest<PolysightAuthService, Void> {

  /**
   * Creates a request to check if the service is available.
   * @param service The service to check.
   * @param templateBuilder The template to use. This allows the caller to specify additional
   * parameters such as timeout.
   */
  public PolysightAuthAvailabilityCheckRequest(
      PolysightAuthService service, RestTemplateBuilder templateBuilder) {
    super(service, templateBuilder);
  }

  @Override
  protected ResponseEntity<Void> executeInternal(
      PolysightAuthService service, RestTemplate template) {
    return template.getForEntity(service.getVersionedUri() + "/ping", Void.class);
  }

}
