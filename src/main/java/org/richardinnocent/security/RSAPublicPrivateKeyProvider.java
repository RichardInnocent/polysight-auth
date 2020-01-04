package org.richardinnocent.security;

import java.io.IOException;
import java.util.Objects;
import org.richardinnocent.util.FileContentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RSAPublicPrivateKeyProvider implements PublicPrivateKeyProvider {

  private final String publicKey;
  private final String privateKey;

  public RSAPublicPrivateKeyProvider(@Value("${rsa.location.public}") String publicKeyLocation,
                                     @Value("${rsa.location.private}") String privateKeyLocation,
                                     FileContentReader fileContentReader)
      throws NullPointerException, IOException {
    publicKey =
        fileContentReader.getFileContentsAtLocation(Objects.requireNonNull(publicKeyLocation));
    privateKey =
        fileContentReader.getFileContentsAtLocation(Objects.requireNonNull(privateKeyLocation));
  }

  @Override
  public String getPublicKey() {
    return publicKey;
  }

  @Override
  public String getPrivateKey() {
    return privateKey;
  }
}
