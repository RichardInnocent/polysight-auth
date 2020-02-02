package org.richardinnocent.security;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import org.junit.Test;
import org.richardinnocent.Profile;
import org.richardinnocent.ProfilesProvider;
import org.richardinnocent.persistence.user.PolysightUserDAO;
import org.richardinnocent.util.FileContentReader;

public class SecurityConfigurationTest {

  private final SecurityConfiguration config = new SecurityConfiguration();

  @Test
  public void testPasswordEncoder() {
    // Ensure encoding method hasn't changed
    assertTrue(
        config.passwordEncoder()
              .matches("password",
                       "$2a$10$NNg6Gstc838Ylto9QrElb.FKTw4tM1l2vjDo/MGqCz5GhdsNvGawW"));
  }

  @Test
  public void testSaleGeneratorIsNotNull() {
    assertEquals(16, config.saltGenerator().generateKey().length());
  }

  @Test
  public void testAuthenticationProvider() {
    assertNotNull(config.authenticationProvider(mock(PolysightUserDAO.class)));
  }

  @Test
  public void testKeyPairGeneratorWithNullSecretLocationThenGeneratorIsStillCreatedOnDevelopmentProfile()
      throws IOException, NoSuchAlgorithmException {
    ProfilesProvider profilesProvider = mock(ProfilesProvider.class);
    when(profilesProvider.isProfileActive(Profile.DEVELOPMENT)).thenReturn(true);
    KeyPairGenerator generator =
        config.keyPairGenerator(null, mock(FileContentReader.class), profilesProvider);
    assertNotNull(generator);
  }

  @Test
  public void testKeyPairGeneratorWithEmptySecretLocationThenGeneratorIsStillCreatedOnDevelopmentProfile()
      throws IOException, NoSuchAlgorithmException {
    ProfilesProvider profilesProvider = mock(ProfilesProvider.class);
    when(profilesProvider.isProfileActive(Profile.DEVELOPMENT)).thenReturn(true);
    KeyPairGenerator generator =
        config.keyPairGenerator("", mock(FileContentReader.class), profilesProvider);
    assertNotNull(generator);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testKeyPairGeneratorWithNullSecretLocationThenGeneratorIsNotCreatedWhenNotOnDevelopmentProfile()
      throws IOException, NoSuchAlgorithmException {
    ProfilesProvider profilesProvider = mock(ProfilesProvider.class);
    when(profilesProvider.isProfileActive(Profile.DEVELOPMENT)).thenReturn(false);
    config.keyPairGenerator(null, mock(FileContentReader.class), profilesProvider);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testKeyPairGeneratorWithEmptySecretLocationThenGeneratorIsNotCreatedWhenNotOnDevelopmentProfile()
      throws IOException, NoSuchAlgorithmException {
    ProfilesProvider profilesProvider = mock(ProfilesProvider.class);
    when(profilesProvider.isProfileActive(Profile.DEVELOPMENT)).thenReturn(false);
    config.keyPairGenerator("", mock(FileContentReader.class), profilesProvider);
  }

  @Test(expected = IOException.class)
  public void testKeyPairGeneratorWithNotFoundSecretLocationThrowsException()
      throws IOException, NoSuchAlgorithmException {
    FileContentReader reader = mock(FileContentReader.class);
    String fakePath = "not a path";
    when(reader.getByteContentsFromFileAtLocation(fakePath)).thenThrow(new IOException());
    config.keyPairGenerator(fakePath, reader, mock(ProfilesProvider.class));
  }

  @Test
  public void testKeyPairGeneratorGeneratesKeyWhenFileFound()
      throws IOException, NoSuchAlgorithmException {
    FileContentReader reader = mock(FileContentReader.class);
    String realPath = "A real path";
    when(reader.getByteContentsFromFileAtLocation(realPath)).thenReturn(new byte[] {(byte) 4});
    assertNotNull(config.keyPairGenerator(realPath, reader, mock(ProfilesProvider.class)));
  }

}