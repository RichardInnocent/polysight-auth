package org.richardinnocent.models.user;

import java.util.Optional;
import org.springframework.security.core.GrantedAuthority;

/**
 * Contains the set of different roles that can be assigned to users.
 */
public enum UserRole {

  /**
   * The user manages the application.
   */
  POLYSIGHT_ADMIN,

  /**
   * The user has access to view and edit their own information and platform experience.
   */
  USER;

  public final GrantedAuthority getAuthority() {
    return (GrantedAuthority) this::name;
  }

  /**
   * Gets the role that matches the given role name. Note that the role is case insensitive.
   * @param roleName The name of the role to search for.
   * @return The appropriate role, or an empty optional if no role exists with the given name.
   */
  public static Optional<UserRole> fromName(String roleName) {
    if (roleName == null) {
      return Optional.empty();
    }
    try {
      return Optional.of(valueOf(roleName.toUpperCase()));
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }
}
