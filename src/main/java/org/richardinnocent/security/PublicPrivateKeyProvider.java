package org.richardinnocent.security;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Interface for retrieving a public/private key pair for use through the application.
 */
public interface PublicPrivateKeyProvider {

  /**
   * Gets the public key.
   * @return The public key.
   */
  PublicKey getPublicKey();

  /**
   * Gets the private key.
   * @return The private key.
   */
  PrivateKey getPrivateKey();
}
