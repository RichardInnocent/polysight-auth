package org.richardinnocent.models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import javax.persistence.*;

import static org.richardinnocent.models.user.PolysightUserConstraints.*;

/**
 * Represents an application user.
 */
@Entity
@Table(
    name = "user",
    uniqueConstraints = {
        @UniqueConstraint(name = "app_user_email", columnNames = "email")
    }
)
public class PolysightUser {

  @Id
  @Column(name = "user_id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "email", nullable = false, unique = true, length = EMAIL_MAX_LENGTH)
  private String email;

  @Column(name = "first_name", nullable = false, length = FIRST_NAME_MAX_LENGTH)
  private String firstName;

  @Column(name = "last_name", nullable = false, length = LAST_NAME_MAX_LENGTH)
  private String lastName;

  @Column(name = "date_of_birth", nullable = false)
  private LocalDate dateOfBirth;

  @Column(name = "creation_time", nullable = false)
  private DateTime creationTime;

  @Column(name = "password", nullable = false, length = 128)
  private String password;

  @Column(name = "password_salt", nullable = false, length = 128)
  private String passwordSalt;

  @Enumerated(EnumType.STRING)
  @Column(name = "account_status", nullable = false, length = 16)
  private AccountStatus accountStatus;

  /**
   * Gets the ID of the user.
   * @return The ID of the user.
   */
  public long getId() {
    return id;
  }

  /**
   * Sets the ID of the user.
   * @param id The ID of the user.
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * Gets the first name of the user.
   * @return The user's first name.
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets the first name of the user.
   * @param firstName The user's first name.
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Gets the last name of the user.
   * @return The user's last name.
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the last name of the user.
   * @param lastName The user's last name.
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Gets the email address of the user.
   * @return The user's email address.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the email address of the user.
   * @param email The user's email address.
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Gets the date of birth of the user.
   * @return The user's date of birth.
   */
  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  /**
   * Sets the date of birth of the user.
   * @param dateOfBirth The user's date of birth.
   */
  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  /**
   * Gets the time that this user was created.
   * @return The time that this user was created.
   */
  public DateTime getCreationTime() {
    return creationTime;
  }

  /**
   * Sets that time that this user was created.
   * @param creationTime The time that this user was created.
   */
  public void setCreationTime(DateTime creationTime) {
    this.creationTime = creationTime.withZone(DateTimeZone.UTC);
  }

  /**
   * Gets the user's encrypted password.
   * @return The user's encrypted password.
   */
  @JsonIgnore // ensure this is never returned in JSON
  public String getPassword() {
    return password;
  }

  /**
   * Gets the user's password.
   * @param password The user's encrypted password.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Sets the salt used to generate this user's encrypted password.
   * @return The salt used to generate this user's encrypted password.
   */
  @JsonIgnore // ensure this is never returned in JSON
  public String getPasswordSalt() {
    return passwordSalt;
  }

  /**
   * Sets the salt used to generate this user's encrypted password.
   * @param passwordSalt The salt used to generate this user's encrypted password.
   */
  public void setPasswordSalt(String passwordSalt) {
    this.passwordSalt = passwordSalt;
  }

  /**
   * Gets the account status for this user.
   * @return The user's account status.
   */
  public AccountStatus getAccountStatus() {
    return accountStatus;
  }

  /**
   * Sets the account status for this user.
   * @param accountStatus The user's account status.
   */
  public void setAccountStatus(AccountStatus accountStatus) {
    this.accountStatus = accountStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PolysightUser)) {
      return false;
    }
    PolysightUser that = (PolysightUser) o;
    return Objects.equals(email, that.email) &&
        Objects.equals(firstName, that.firstName) &&
        Objects.equals(lastName, that.lastName) &&
        Objects.equals(dateOfBirth, that.dateOfBirth) &&
        Objects.equals(creationTime, that.creationTime) &&
        Objects.equals(password, that.password) &&
        Objects.equals(passwordSalt, that.passwordSalt) &&
        accountStatus == that.accountStatus;
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(email,
              firstName,
              lastName,
              dateOfBirth,
              creationTime,
              password,
              passwordSalt,
              accountStatus);
  }
}
