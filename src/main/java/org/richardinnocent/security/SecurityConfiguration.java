package org.richardinnocent.security;

import java.io.IOException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import org.richardinnocent.Profile;
import org.richardinnocent.ProfilesProvider;
import org.richardinnocent.PropertiesKey;
import org.richardinnocent.util.FileContentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Houses security-related beans.
 */
@Configuration
public class SecurityConfiguration {

  /**
   * Gets the encoder that is used to encode raw passwords.
   * @return The password encoder.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Gets the password salt generator.
   * @return The password salt generator.
   */
  @Bean
  public StringKeyGenerator saltGenerator() {
    return KeyGenerators.string();
  }

  /**
   * Gets the provider that authenticates users.
   * @param userService The user service object that reads and builds authentication information.
   * @return The provider that authenticates users.
   */
  @Bean
  public AuthenticationProvider authenticationProvider(UserDetailsService userService) {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    authenticationProvider.setUserDetailsService(userService);
    return authenticationProvider;
  }

  /**
   * The key-pair generator used to generate the public/private keys that sign the JWTs.
   * @param secretLocation The location of the file containing the secret. This ensures that all
   * JWTs generated and decrypted where on applications that share this secret.
   * @param fileContentReader A reader to assist with acquiring file content.
   * @param profilesProvider Provides the currently active profiles. This allows the secret to be
   * omitted only when the {@link Profile#DEVELOPMENT} profile is active.
   * @return A new key-apri generator.
   * @throws IllegalArgumentException Thrown if the secret is {@code null} or empty, and the
   * {@link Profile#DEVELOPMENT} profile is not active.
   * @throws IOException Thrown if the file does not exist at the specified location, or cannot be
   * read.
   * @throws NoSuchAlgorithmException Thrown if the required algorithm is not recognised.
   */
  @Bean
  public KeyPairGenerator keyPairGenerator(
      @Value("${" + PropertiesKey.JWT_SECRET_LOCATION + "}") String secretLocation,
      FileContentReader fileContentReader,
      ProfilesProvider profilesProvider)
      throws IllegalArgumentException, IOException, NoSuchAlgorithmException {
    KeyPairGenerator generator = KeyPairGenerator.getInstance("EC");
    if ((secretLocation == null || secretLocation.isEmpty())) {
      if (!profilesProvider.isAnyProfileActive(Profile.DEVELOPMENT, Profile.UNIT_TEST)) {
        throw new IllegalArgumentException(
            "The JWT secret location is undefined or is null. This would cause a key pair to be "
                + "created without a secret which is unacceptable on this configuration as this "
                + "would prevent horizontal scaling");
      } else {
        generator.initialize(512);
      }
    } else {
      byte[] bytes =
          fileContentReader.getByteContentsFromFileAtLocation(secretLocation);
      generator.initialize(512, new SecureRandom(bytes));
    }
    return generator;
  }

}
