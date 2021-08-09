package org.richardinnocent.polysight.auth.server.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;
import org.richardinnocent.polysight.auth.server.Profile;
import org.richardinnocent.polysight.auth.server.ProfilesProvider;
import org.richardinnocent.polysight.auth.server.util.FileContentReader;
import org.springframework.security.core.userdetails.UserDetailsService;

public class SecurityConfigurationTest {

  private final SecurityConfiguration config = new SecurityConfiguration();

  @Test
  public void testPasswordEncoder() {
    // Ensure encoding method hasn't changed
    assertTrue(
        config.passwordEncoder()
              .matches("password",
                       "$2a$10$NNg6Gstc838Ylto9QrElb.FKTw4tM1l2vjDo/MGqCz5GhdsNvGawW"
              )
    );
  }

  @Test
  public void testSaleGeneratorIsNotNull() {
    assertEquals(16, config.saltGenerator().generateKey().length());
  }

  @Test
  public void testAuthenticationProvider() {
    assertNotNull(config.authenticationProvider(mock(UserDetailsService.class)));
  }

  @Test
  public void testKeyPairGeneratorWithNullSecretLocationThenGeneratorIsStillCreatedOnDevelopmentProfile()
      throws IOException, NoSuchAlgorithmException {
    ProfilesProvider profilesProvider = mock(ProfilesProvider.class);
    configureSecretlessProfiling(profilesProvider, true);
    KeyPairGenerator generator =
        config.keyPairGenerator(null, mock(FileContentReader.class), profilesProvider);
    assertNotNull(generator);
  }

  @Test
  public void testKeyPairGeneratorWithEmptySecretLocationThenGeneratorIsStillCreatedOnDevelopmentProfile()
      throws IOException, NoSuchAlgorithmException {
    ProfilesProvider profilesProvider = mock(ProfilesProvider.class);
    configureSecretlessProfiling(profilesProvider, true);
    KeyPairGenerator generator =
        config.keyPairGenerator("", mock(FileContentReader.class), profilesProvider);
    assertNotNull(generator);
  }

  @Test
  public void testKeyPairGeneratorWithNullSecretLocationThenGeneratorIsNotCreatedWhenNotOnDevelopmentProfile() {
    ProfilesProvider profilesProvider = mock(ProfilesProvider.class);
    configureSecretlessProfiling(profilesProvider, false);
    assertThrows(
        IllegalArgumentException.class,
        () -> config.keyPairGenerator(null, mock(FileContentReader.class), profilesProvider)
    );
  }

  @Test
  public void testKeyPairGeneratorWithEmptySecretLocationThenGeneratorIsNotCreatedWhenNotOnDevelopmentProfile() {
    ProfilesProvider profilesProvider = mock(ProfilesProvider.class);
    configureSecretlessProfiling(profilesProvider, false);
    assertThrows(
        IllegalArgumentException.class,
        () -> config.keyPairGenerator("", mock(FileContentReader.class), profilesProvider)
    );
  }

  @Test
  public void testKeyPairGeneratorWithNotFoundSecretLocationThrowsException() throws IOException {
    FileContentReader reader = mock(FileContentReader.class);
    String fakePath = "not a path";
    when(reader.getByteContentsFromFileAtLocation(fakePath)).thenThrow(new IOException());
    assertThrows(
        IOException.class,
        () -> config.keyPairGenerator(fakePath, reader, mock(ProfilesProvider.class))
    );
  }

  @Test
  public void testKeyPairGeneratorGeneratesKeyWhenFileFound()
      throws IOException, NoSuchAlgorithmException {
    FileContentReader reader = mock(FileContentReader.class);
    String realPath = "A real path";
    when(reader.getByteContentsFromFileAtLocation(realPath)).thenReturn(new byte[] {(byte) 4});
    assertNotNull(config.keyPairGenerator(realPath, reader, mock(ProfilesProvider.class)));
  }

  private void configureSecretlessProfiling(ProfilesProvider profilesProvider, boolean enabled) {
    when(profilesProvider.isAnyProfileActive(Profile.DEVELOPMENT, Profile.UNIT_TEST))
        .thenReturn(enabled);
  }

}