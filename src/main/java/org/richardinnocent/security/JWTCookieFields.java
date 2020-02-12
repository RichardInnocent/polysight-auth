package org.richardinnocent.security;

/**
 * Class of JWT cookie fields/keys.
 */
public class JWTCookieFields {

  /**
   * The name of the authentication cookie.
   */
  public static final String COOKIE_NAME = "polysight-token";

  /**
   * The issuer that should be specified on all issued JWTs.
   */
  public static final String ISSUER = "polysight";

  /**
   * The name of the claim that contains the user's email.
   */
  public static final String EMAIL_CLAIM_KEY = "email";

  /**
   * The name of the claim that contains the user's roles.
   */
  public static final String AUTHORITIES_CLAIM_KEY = "authorities";

}
