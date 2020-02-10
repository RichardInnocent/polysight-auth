package org.richardinnocent.persistence.user;

import java.util.List;
import javax.transaction.Transactional;
import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.models.user.UserRole;
import org.richardinnocent.persistence.EntityDAO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class UserRoleAssignmentDAO extends EntityDAO<UserRole> {

  private final UserRoleRepository userRoleRepo;

  public UserRoleAssignmentDAO(UserRoleRepository userRoleRepo) {
    super(UserRole.class);
    this.userRoleRepo = userRoleRepo;
  }

  /**
   * Finds all roles for a particular user.
   * @param user The user whose roles should be searched for.
   * @return All roles for the user.
   */
  public List<UserRole> findAllRolesForUser(PolysightUser user) {
    return findAllRolesForUserWithId(user.getId());
  }

  /**
   * Finds all roles for a particular user.
   * @param userId The ID of the user whose roles should be searched for.
   * @return All roles for the user.
   */
  public List<UserRole> findAllRolesForUserWithId(long userId) {
    return userRoleRepo.findAll(hasUserId(userId));
  }

  private Specification<UserRole> hasUserId(long userId) {
    return (role, cq, cb) -> cb.equal(role.get("user_id"), userId);
  }

}
