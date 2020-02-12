package org.richardinnocent.services.user.creation;

import javax.transaction.Transactional;
import org.joda.time.DateTime;
import org.richardinnocent.models.user.AccountStatus;
import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.models.user.RawPolysightUser;
import org.richardinnocent.models.user.UserRole;
import org.richardinnocent.models.user.UserRoleAssignment;
import org.richardinnocent.persistence.exception.InsertionException;
import org.richardinnocent.persistence.user.PolysightUserDAO;
import org.richardinnocent.persistence.user.UserRoleAssignmentDAO;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service used to create new users.
 */
@Service
public class UserCreationService {

  private final PolysightUserDAO userDao;
  private final UserRoleAssignmentDAO userRoleAssignmentDAO;
  private final PasswordEncoder passwordEncoder;
  private final StringKeyGenerator saltGenerator;

  /**
   * Creates a new user creation service.
   * @param encoder The encoder used to encode user's raw passwords.
   * @param saltGenerator The instance used to create password salts.
   * @param userDao The DAO for user entities.
   * @param userRoleAssignmentDAO The DAO for user roles.
   */
  public UserCreationService(PasswordEncoder encoder,
                             StringKeyGenerator saltGenerator,
                             PolysightUserDAO userDao,
                             UserRoleAssignmentDAO userRoleAssignmentDAO) {
    this.passwordEncoder = encoder;
    this.saltGenerator = saltGenerator;
    this.userDao = userDao;
    this.userRoleAssignmentDAO = userRoleAssignmentDAO;
  }

  /**
   * Creates a new user from the raw entity type.
   * @param rawUser The raw user entity, straight from the create account page.
   * @return The created user.
   * @throws InsertionException Thrown if there is a problem persisting the user.s
   */
  @Transactional
  public PolysightUser createUser(RawPolysightUser rawUser) throws InsertionException {
    PolysightUser user = new PolysightUser();
    user.setFirstName(rawUser.getFirstName());
    user.setLastName(rawUser.getLastName());
    user.setEmail(rawUser.getEmail());
    user.setDateOfBirth(rawUser.getDateOfBirth());
    user.setCreationTime(DateTime.now());
    user.setPasswordSalt(saltGenerator.generateKey());
    user.setPassword(passwordEncoder.encode(rawUser.getPassword() + user.getPasswordSalt()));
    user.setAccountStatus(AccountStatus.ACTIVE);

    userDao.save(user);

    UserRoleAssignment roleAssignment = new UserRoleAssignment();
    roleAssignment.setUserRole(UserRole.USER);
    roleAssignment.setUserId(user.getId());
    userRoleAssignmentDAO.save(roleAssignment);
    return user;
  }

}
