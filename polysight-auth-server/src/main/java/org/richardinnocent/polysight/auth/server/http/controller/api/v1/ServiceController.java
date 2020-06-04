package org.richardinnocent.polysight.auth.server.http.controller.api.v1;

import java.security.interfaces.ECPublicKey;
import org.richardinnocent.polysight.auth.server.security.PublicPrivateKeyProvider;
import org.richardinnocent.polysight.auth.v0_0_3_alpha.model.security.ECPublicKeyResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to return specific information about the server.
 */
@RestController
@RequestMapping("/api/v1")
public class ServiceController {

  private final PublicPrivateKeyProvider keyProvider;

  public ServiceController(PublicPrivateKeyProvider keyProvider) {
    this.keyProvider = keyProvider;
  }

  /**
   * Pings the server to check that the application is still working correctly.
   */
  @GetMapping("/ping")
  @SuppressWarnings("unused")
  public void ping() {}

  /**
   * For other Polysight applications to be able to decode the JWT that is generated through this
   * authentication service, the public key of this service needs to be exposed.
   * @return The public key of this service.
   */
  @GetMapping(value = "/publickey", produces = MediaType.APPLICATION_JSON_VALUE)
  @SuppressWarnings("unused")
  public ECPublicKeyResponse publicKey() {
    ECPublicKey key = (ECPublicKey) keyProvider.getPublicKey();
    ECPublicKeyResponse response = new ECPublicKeyResponse();
    response.setAlgorithm(key.getAlgorithm());
    response.setFormat(key.getFormat());
    response.setKey(key.getEncoded());
    response.setW(key.getW());
    response.setParams(key.getParams());
    return response;
  }

}
