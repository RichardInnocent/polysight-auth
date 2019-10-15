package org.richardinnocent.models.user;

import org.joda.time.LocalDate;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class RawPolysightUser {

  @NotNull(message = "Name must be specified")
  private String fullName;

  @NotNull(message = "Email must be specified")
  private String email;

  @NotNull(message = "Date of birth must be specified")
  private LocalDate dateOfBirth;

  @NotNull(message = "Password must be specified")
  private CharSequence password;

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public CharSequence getPassword() {
    return password;
  }

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
