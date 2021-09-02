package org.richardinnocent.polysight.auth.server.security;

/**
 * Class of JWT cookie fields/keys.
 */
public class JwtFields {

  /**
   * The name of the authentication cookie.
   */
  public static final String HEADER_NAME = "Authorization";

  /**
   * The issuer that should be specified on all issued JWTs.
   */
  public static final String ISSUER = "polysight";

  /**
   * The name of the claim that contains the user's email.
   */
  public static final String EMAIL_CLAIM_KEY = "email";

  /**
   * The ID of the user.
   */
  public static final String USER_ID_CLAIM_KEY = "id";

  /**
   * The name of the claim that contains the user's roles.
   */
  public static final String AUTHORITIES_CLAIM_KEY = "authorities";

  private JwtFields() {}

}
