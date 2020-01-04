package org.richardinnocent.security;

import org.springframework.stereotype.Component;

public interface PublicPrivateKeyProvider {
  String getPublicKey();
  String getPrivateKey();
}
