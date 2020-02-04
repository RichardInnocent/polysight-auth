package org.richardinnocent.models.user;

/**
 * Defines a set of different states in which a user account can exist.
 */
public enum AccountStatus {

  /**
   * The user is able to use the system as normal.
   */
  ACTIVE(true),

  /**
   * The user has been disabled.
   */
  DISABLED(false);

  final boolean ableToLogOn;

  AccountStatus(boolean ableToLogOn) {
    this.ableToLogOn = ableToLogOn;
  }

  /**
   * Determines whether the user is able to log in.
   * @return Whether the user is able to log in.
   */
  public boolean isAbleToLogOn() {
    return ableToLogOn;
  }

}
