package org.richardinnocent.polysight.auth.server;

/**
 * Keys for {@link java.util.Properties} fields used throughout the application.
 */
public class PropertiesKey {

  /**
   * The location of the secret used to generate the public and private EC keys used to encrypt the
   * JWT tokens.
   */
  public static final String JWT_SECRET_LOCATION = "jwt.key.secret.location";
}
