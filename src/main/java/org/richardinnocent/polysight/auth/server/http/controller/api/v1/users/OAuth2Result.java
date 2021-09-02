package org.richardinnocent.polysight.auth.server.http.controller.api.v1.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 * The result of a token generation request.
 */
public class OAuth2Result {

  @JsonProperty("access_token")
  private final String accessToken;

  @JsonProperty("token_type")
  private final String tokenType;

  @JsonProperty("expires_in")
  private final int durationValidSeconds;

  private OAuth2Result(String accessToken, String tokenType, int expirationTime) {
    this.accessToken = accessToken;
    this.tokenType = tokenType;
    this.durationValidSeconds = expirationTime;
  }

  /**
   * Gets the token that can be used to authenticate.
   * @return The token that can be used to authenticate.
   */
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * Gets the type of the token.
   * @return The type of the token.
   */
  public String getTokenType() {
    return tokenType;
  }

  /**
   * Gets the number of seconds that the token is valid for.
   * @return The number of seconds that the token is valid for.
   */
  public int getDurationValidSeconds() {
    return durationValidSeconds;
  }

  /**
   * Creates an OAuth 2.0 result for a JWT.
   * @param accessToken The JWT.
   * @param durationValidSeconds The number of seconds that the JWT is valid for.
   * @return An appropriate OAuth 2.0 result.
   */
  public static OAuth2Result forJwt(String accessToken, int durationValidSeconds) {
    return new OAuth2Result(accessToken, "jwt", durationValidSeconds);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof OAuth2Result)) {
      return false;
    }
    OAuth2Result that = (OAuth2Result) o;
    return durationValidSeconds == that.durationValidSeconds
        && Objects.equals(accessToken, that.accessToken)
        && Objects.equals(tokenType, that.tokenType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessToken, tokenType, durationValidSeconds);
  }
}
