package org.richardinnocent.models.user;

import javax.validation.constraints.Size;
import org.joda.time.LocalDate;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Represents a user whose account is in the processes of being created.
 */
public class RawPolysightUser {

  @NotNull(message = "Name must be specified")
  private String fullName;

  @NotNull(message = "Email must be specified")
  @Size(min=3, message = "A valid email address must be at least 3 characters")
  private String email;

  @NotNull(message = "Date of birth must be specified")
  private LocalDate dateOfBirth;

  @NotNull(message = "Password must be specified")
  @Size(min=8, message = "Password must be at least 8 characters")
  private CharSequence password;

  /**
   * Gets the full name of the user.
   * @return The full name of the user.
   */
  public String getFullName() {
    return fullName;
  }

  /**
   * Sets the full name of the user.
   * @param fullName The full name of the user.
   */
  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  /**
   * Gets the email address of the user.
   * @return The email address of the user.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the email address of the user.
   * @param email The email address of the user.
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Gets the date of birth of the user.
   * @return The date of birth of the user.
   */
  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  /**
   * Sets the date of birth of the user.
   * @param dateOfBirth The date of birth of the user.
   */
  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  /**
   * Gets the raw, unencrypted password of the user.
   * @return The user's password.
   */
  public CharSequence getPassword() {
    return password;
  }

  /**
   * Sets the raw, unencrypted password of the user.
   * @param password The user's password.
   */
  public void setPassword(CharSequence password) {
    this.password = password;
  }

  @Override
  public final int hashCode() {
    return Objects.hashCode(email);
  }

  @Override
  public final boolean equals(Object other) {
    return other instanceof RawPolysightUser && equalsUser((RawPolysightUser) other);
  }

  private boolean equalsUser(RawPolysightUser other) {
    return Objects.equals(email, other.email) &&
        Objects.equals(fullName, other.fullName) &&
        Objects.equals(dateOfBirth, other.dateOfBirth) &&
        Objects.equals(password, other.password);
  }
}
