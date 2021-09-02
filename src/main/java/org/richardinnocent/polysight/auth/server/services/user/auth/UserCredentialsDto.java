package org.richardinnocent.polysight.auth.server.services.user.auth;

import java.util.Objects;
import javax.validation.constraints.NotEmpty;

/**
 * Object represents the user's login credentials.
 */
public class UserCredentialsDto {

  @NotEmpty
  private String email;

  @NotEmpty
  private String password;

  /**
   * Gets the user's email address.
   * @return The user's email address.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the user's email address.
   * @param email The user's email address.
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Gets the user's unencrypted password.
   * @return The user's unencrypted password.
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the user's unencrypted password.
   * @param password The user's unencrypted password.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserCredentialsDto)) {
      return false;
    }
    UserCredentialsDto that = (UserCredentialsDto) o;
    return Objects.equals(email, that.email) && Objects.equals(password, that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, password);
  }
}
