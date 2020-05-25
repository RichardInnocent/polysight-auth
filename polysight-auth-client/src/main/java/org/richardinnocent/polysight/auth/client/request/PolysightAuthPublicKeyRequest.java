package org.richardinnocent.polysight.auth.client.request;

import org.richardinnocent.polysight.auth.client.service.PolysightAuthService;
import org.richardinnocent.polysight.auth.v0_0_3_alpha.model.security.ECPublicKeyResponse;
import org.richardinnocent.polysight.core.client.request.RestTemplateServiceRequest;
import org.richardinnocent.polysight.core.client.service.MajorOnlyServiceVersion;
import org.richardinnocent.polysight.core.client.service.PolysightServiceConfiguration;
import org.richardinnocent.polysight.core.common.mapper.ApiObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * A request to retrieve the public key from a Polysight Auth server.
 */
public class PolysightAuthPublicKeyRequest
    extends RestTemplateServiceRequest<PolysightAuthService, ECPublicKeyResponse> {

  /**
   * Creates a new request to retrieve the public key for Polysight Auth.
   * @param service The service to execute the request against.
   * @param templateBuilder The template builder, detailing specific requirements about the request.
   */
  public PolysightAuthPublicKeyRequest(
      PolysightAuthService service, RestTemplateBuilder templateBuilder) {
    super(service, templateBuilder);
  }

  @Override
  protected ResponseEntity<ECPublicKeyResponse> executeInternal(
      PolysightAuthService service, RestTemplate template) {
    return template.getForEntity(service.getVersionedUri() + "/publickey", ECPublicKeyResponse.class);
  }

}
