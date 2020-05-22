package org.richardinnocent.polysight.auth.server.models.user;

import static org.richardinnocent.polysight.auth.server.models.user.PolysightUserConstraints.*;

import javax.validation.constraints.Size;
import org.joda.time.LocalDate;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Represents a user whose account is in the processes of being created.
 */
public class RawPolysightUser {

  @NotNull(message = "First name must be specified")
  @Size(min = 1,
        max = FIRST_NAME_MAX_LENGTH,
        message = "A valid first name must be between 1 and " + FIRST_NAME_MAX_LENGTH
            + " characters (inclusive)")
  private String firstName;

  @NotNull(message = "Surname must be specified")
  @Size(min = 1,
        max = LAST_NAME_MAX_LENGTH,
        message = "A valid surname must be between 1 and " + LAST_NAME_MAX_LENGTH
            + " characters (inclusive)")
  private String lastName;

  @NotNull(message = "Email must be specified")
  @Size(min = 3,
        max = EMAIL_MAX_LENGTH,
        message = "A valid email address must be between 3 characters and " + EMAIL_MAX_LENGTH
            + " characters (inclusive)")
  private String email;

  @NotNull(message = "Date of birth must be specified")
  private LocalDate dateOfBirth;

  @NotNull(message = "Password must be specified")
  @Size(min=8, message = "Password must be at least 8 characters")
  private CharSequence password;

  /**
   * Gets the first name of the user.
   * @return The first name of the user.
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets the first name of the user.
   * @param firstName The first name of the user.
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Gets the last name of the user.
   * @return The last name of the user.
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the last name of the user.
   * @param lastName The last name of the user.
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
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
        Objects.equals(firstName, other.firstName) &&
        Objects.equals(lastName, other.lastName) &&
        Objects.equals(dateOfBirth, other.dateOfBirth) &&
        Objects.equals(password, other.password);
  }
}
