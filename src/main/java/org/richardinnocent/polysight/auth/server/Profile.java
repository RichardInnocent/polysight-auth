package org.richardinnocent.polysight.auth.server;

import java.util.Optional;

/**
 * The different Spring profiles that are recognised throughout the application.
 */
public enum Profile {
  UNIT_TEST,
  DEVELOPMENT,
  QA,
  PRODUCTION;

  /**
   * Gets the profile with the given name, or returns an empty {@code Optional} if no such profile
   * exists.
   * @param name The profile name to search for.
   * @return An appropriate profile, or an empty {@code Optional} if no such profile exists.
   */
  public static Optional<Profile> fromName(String name) {
    if (name == null) {
      return Optional.empty();
    }
    try {
      return Optional.of(valueOf(name.toUpperCase()));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }
}
