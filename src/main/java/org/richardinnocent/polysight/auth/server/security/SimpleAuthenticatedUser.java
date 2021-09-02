package org.richardinnocent.polysight.auth.server.security;

import java.util.Objects;

/**
 * A simple representation of a user. This information can be deciphered directly from a JWT.
 */
public class SimpleAuthenticatedUser {

  private final long id;
  private final String emailAddress;

  private SimpleAuthenticatedUser(long id, String emailAddress) {
    this.id = id;
    this.emailAddress = emailAddress;
  }

  /**
   * Creates a new simple authenticated user representation of a user.
   * @param id The user's ID.
   * @param emailAddress The user's email address.
   * @return A simple authenticated user.
   */
  public static SimpleAuthenticatedUser of(long id, String emailAddress) {
    return new SimpleAuthenticatedUser(id, emailAddress);
  }

  /**
   * Gets the ID of the user.
   * @return The ID of the user.
   */
  public long getId() {
    return id;
  }

  /**
   * Gets the user's email address.
   * @return The user's email address.
   */
  public String getEmailAddress() {
    return emailAddress;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof SimpleAuthenticatedUser)) {
      return false;
    }
    SimpleAuthenticatedUser that = (SimpleAuthenticatedUser) o;
    return id == that.id && Objects.equals(emailAddress, that.emailAddress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, emailAddress);
  }
}
