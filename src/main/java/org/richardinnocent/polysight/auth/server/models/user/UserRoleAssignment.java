package org.richardinnocent.polysight.auth.server.models.user;

import static org.richardinnocent.polysight.auth.server.models.user.UserRoleConstraints.*;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Represents a role assigned to a user. Note that many roles can be assigned to many users.
 */
@Entity
@Table(
    name = "user_role_assignment",
    uniqueConstraints = {
        @UniqueConstraint(name = "unique_user_role_pair",
                          columnNames = {"assignment_id", "user_id"})
    }
)
@SuppressWarnings("unused")
public class UserRoleAssignment {

  @Id
  @Column(name = "assignment_id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "user_id", nullable = false)
  private long userId;

  @Column(name = "user_role", nullable = false, length = USER_ROLE_MAX_LENGTH)
  @Enumerated(EnumType.STRING)
  private UserRole userRole;

  /**
   * Gets the assignment ID.
   * @return The assignment ID.
   */
  public long getId() {
    return id;
  }

  /**
   * Sets the assignment ID.
   * @param id The assignment ID.
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * Gets the ID of the user that owns the assigned role.
   * @return The ID of the user.
   */
  public long getUserId() {
    return userId;
  }

  /**
   * Sets the ID of the user that owns the assigned role.
   * @param userId The ID of the user.
   */
  public void setUserId(long userId) {
    this.userId = userId;
  }

  /**
   * Gets the role assigned to the user.
   * @return The role assigned to the user.
   */
  public UserRole getUserRole() {
    return userRole;
  }

  /**
   * Sets the role assigned to the user.
   * @param userRole The role assigned to the user.
   */
  public void setUserRole(UserRole userRole) {
    this.userRole = userRole;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UserRoleAssignment)) {
      return false;
    }
    UserRoleAssignment that = (UserRoleAssignment) o;
    return userId == that.userId &&
        userRole == that.userRole;
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, userRole);
  }
}
