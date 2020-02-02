package org.richardinnocent.security;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.junit.Test;

public class JWTPublicPrivateKeyProviderTest {

  @Test
  public void testPublicAndPrivateKeyAreDetectedFromKeyPairGenerator() {
    KeyPairGenerator keyPairGenerator = mock(KeyPairGenerator.class);
    PublicKey publicKey = mock(PublicKey.class);
    PrivateKey privateKey = mock(PrivateKey.class);
    KeyPair keyPair = new KeyPair(publicKey, privateKey);
    when(keyPairGenerator.generateKeyPair()).thenReturn(keyPair);

    JWTPublicPrivateKeyProvider keyProvider = new JWTPublicPrivateKeyProvider(keyPairGenerator);
    assertEquals(publicKey, keyProvider.getPublicKey());
    assertEquals(privateKey, keyProvider.getPrivateKey());
  }

}