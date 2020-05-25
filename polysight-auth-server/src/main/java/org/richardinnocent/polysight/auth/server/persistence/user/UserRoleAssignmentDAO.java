package org.richardinnocent.polysight.auth.server.persistence.user;

import java.util.List;
import javax.transaction.Transactional;
import org.richardinnocent.polysight.auth.server.models.user.PolysightUser;
import org.richardinnocent.polysight.auth.server.models.user.UserRoleAssignment;
import org.richardinnocent.polysight.auth.server.persistence.EntityDAO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class UserRoleAssignmentDAO extends EntityDAO<UserRoleAssignment> {

  private final UserRoleAssignmentRepository userRoleRepo;

  public UserRoleAssignmentDAO(UserRoleAssignmentRepository userRoleRepo) {
    super(UserRoleAssignment.class);
    this.userRoleRepo = userRoleRepo;
  }

  /**
   * Finds all roles for a particular user.
   * @param user The user whose roles should be searched for.
   * @return All roles for the user.
   */
  public List<UserRoleAssignment> findAllRolesForUser(PolysightUser user) {
    return findAllRolesForUserWithId(user.getId());
  }

  /**
   * Finds all roles for a particular user.
   * @param userId The ID of the user whose roles should be searched for.
   * @return All roles for the user.
   */
  public List<UserRoleAssignment> findAllRolesForUserWithId(long userId) {
    return userRoleRepo.findAll(hasUserId(userId));
  }

  private Specification<UserRoleAssignment> hasUserId(long userId) {
    return (role, cq, cb) -> cb.equal(role.get("userId"), userId);
  }

}
