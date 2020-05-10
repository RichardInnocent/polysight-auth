package org.richardinnocent.http.controller.api.v1;

import java.security.PublicKey;
import org.richardinnocent.security.PublicPrivateKeyProvider;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides other applications with access to the required security details.
 */
@RestController
@RequestMapping("/api/v1/security")
public class SecurityController {

  private final PublicPrivateKeyProvider keyProvider;

  public SecurityController(PublicPrivateKeyProvider keyProvider) {
    this.keyProvider = keyProvider;
  }

  /**
   * For other Polysight applications to be able to decode the JWT that is generated through this
   * authentication service, the public key of this service needs to be exposed.
   * @return The public key of this service.
   */
  @GetMapping(value = "/publickey", produces = MediaType.APPLICATION_JSON_VALUE)
  @SuppressWarnings("unused")
  public PublicKeyResponse publicKey() {
    return new PublicKeyResponse(keyProvider.getPublicKey());
  }

  @SuppressWarnings("unused") // Accessed via ObjectMapper
  private static class PublicKeyResponse {
    private final String algorithm;
    private final String format;
    private final byte[] key;

    private PublicKeyResponse(PublicKey publicKey) {
      this.algorithm = publicKey.getAlgorithm();
      this.key = publicKey.getEncoded();
      this.format = publicKey.getFormat();
    }

    public String getAlgorithm() {
      return algorithm;
    }

    public String getFormat() {
      return format;
    }

    public byte[] getKey() {
      return key;
    }
  }

}
