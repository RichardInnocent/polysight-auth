package org.richardinnocent.polysight.auth.server.http.controller.api.v1.users;

import java.time.LocalDate;
import java.util.Objects;
import org.richardinnocent.polysight.auth.server.models.user.AccountStatus;
import org.richardinnocent.polysight.auth.server.models.user.PolysightUser;

public class UserDto {
  private final long id;
  private final String email;
  private final String firstName;
  private final String lastName;
  private final LocalDate dateOfBirth;
  private final AccountStatus accountStatus;

  private UserDto(PolysightUser user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.dateOfBirth = user.getDateOfBirth();
    this.accountStatus = user.getAccountStatus();
  }

  /**
   * Creates a DTO to represent the given user object.
   * @param user The user to represent.
   * @return The user to represent.
   * @throws NullPointerException Thrown if {@code user == null}.
   */
  public static UserDto forUser(PolysightUser user) throws NullPointerException {
    return new UserDto(user);
  }

  /**
   * Gets the ID of the user.
   * @return The ID of the user.
   */
  public long getId() {
    return id;
  }

  /**
   * Gets the email address of the user.
   * @return The email address of the user.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Gets the user's first name.
   * @return The user's first name.
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Gets the user's last name.
   * @return The user's last name.
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Gets the user's date of birth.
   * @return The user's date of birth.
   */
  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  /**
   * Gets the user account status.
   * @return The user's account status.
   */
  public AccountStatus getAccountStatus() {
    return accountStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserDto)) {
      return false;
    }
    UserDto userDto = (UserDto) o;
    return id == userDto.id
        && Objects.equals(email, userDto.email)
        && Objects.equals(firstName, userDto.firstName)
        && Objects.equals(lastName, userDto.lastName)
        && Objects.equals(dateOfBirth, userDto.dateOfBirth)
        && accountStatus == userDto.accountStatus;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, firstName, lastName, dateOfBirth, accountStatus);
  }
}
