package org.richardinnocent.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.richardinnocent.Qualifiers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Class to generate a public/private key pair for use specifically with generating/decoding JWT
 * tokens.
 */
@Component
@Qualifier(Qualifiers.JWT)
public class JWTPublicPrivateKeyProvider implements PublicPrivateKeyProvider {

  private final PublicKey publicKey;
  private final PrivateKey privateKey;

  /**
   * Creates a new key provider using the provided key generation method.
   * @param generator The method with which to generate the keys.
   */
  public JWTPublicPrivateKeyProvider(KeyPairGenerator generator) {
    KeyPair keyPair = generator.generateKeyPair();
    publicKey = keyPair.getPublic();
    privateKey = keyPair.getPrivate();
  }

  @Override
  public PublicKey getPublicKey() {
    return publicKey;
  }

  @Override
  public PrivateKey getPrivateKey() {
    return privateKey;
  }
}
