package org.richardinnocent.polysight.auth.server.services.user.auth;

import java.util.Objects;

public class Token {

  private final String token;
  private final int maxAgeSeconds;

  public Token(String token, int maxAgeSeconds)
      throws NullPointerException, IllegalArgumentException {
    this.token = Objects.requireNonNull(token, "Token is null");
    if (maxAgeSeconds < 0) {
      throw new IllegalArgumentException(
          "Max age of the token cannot be < 0 but was " + maxAgeSeconds
      );
    }
    this.maxAgeSeconds = maxAgeSeconds;
  }

  public String getToken() {
    return token;
  }

  public int getMaxAgeSeconds() {
    return maxAgeSeconds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Token)) {
      return false;
    }
    Token token1 = (Token) o;
    return maxAgeSeconds == token1.maxAgeSeconds && Objects.equals(token, token1.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token, maxAgeSeconds);
  }
}
