package org.richardinnocent.services.user;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.joda.time.DateTime;
import org.richardinnocent.models.user.AccountStatus;
import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.models.user.RawPolysightUser;
import org.richardinnocent.models.user.UserRole;
import org.richardinnocent.models.user.UserRoleAssignment;
import org.richardinnocent.persistence.exception.DeletionException;
import org.richardinnocent.persistence.exception.InsertionException;
import org.richardinnocent.persistence.exception.NotFoundException;
import org.richardinnocent.persistence.exception.ReadException;
import org.richardinnocent.persistence.user.PolysightUserDAO;
import org.richardinnocent.persistence.user.UserRoleAssignmentDAO;
import org.richardinnocent.services.user.creation.UserAlreadyExistsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

  private final PolysightUserDAO userDao;
  private final UserRoleAssignmentDAO userRoleAssignmentDAO;
  private final PasswordEncoder passwordEncoder;
  private final StringKeyGenerator saltGenerator;

  public UserService(PolysightUserDAO userDao,
                     UserRoleAssignmentDAO userRoleAssignmentDAO,
                     PasswordEncoder passwordEncoder,
                     StringKeyGenerator saltGenerator) {
    this.userDao = userDao;
    this.userRoleAssignmentDAO = userRoleAssignmentDAO;
    this.passwordEncoder = passwordEncoder;
    this.saltGenerator = saltGenerator;
  }

  /**
   * Gets the user with the given ID.
   * @param id The ID of the user to search for.
   * @return The user, or an empty optional if no user with the provided {@code id} exists.
   * @throws ReadException Thrown if there is a problem accessing the database.
   */
  public Optional<PolysightUser> findById(long id) throws ReadException {
    return userDao.findById(id);
  }

  /**
   * Gets the user with the given email.
   * @param email The email of the user to search for.
   * @return The user, or an empty optional if no user with the provided {@code email} exists.
   * @throws ReadException Thrown if there is a problem accessing the database.
   */
  public Optional<PolysightUser> findByEmail(String email) throws ReadException {
    return userDao.findByEmail(email);
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<PolysightUser> userOptional = userDao.findByEmail(email);
    if (userOptional.isPresent()) {
      PolysightUser user = userOptional.get();
      List<GrantedAuthority> authorities = getAuthoritiesForUser(user);
      return new User(user.getEmail(), user.getPassword(), authorities);
    } else {
      throw new UsernameNotFoundException("User with email " + email + " not found");
    }
  }

  private List<GrantedAuthority> getAuthoritiesForUser(PolysightUser user) {
    return userRoleAssignmentDAO.findAllRolesForUser(user)
                                .stream()
                                .map(UserRoleAssignment::getUserRole)
                                .map(UserRole::getAuthority)
                                .collect(Collectors.toList());
  }

  /**
   * Creates a new user from the raw entity type.
   * @param rawUser The raw user entity, straight from the create account page.
   * @return The created user.
   * @throws InsertionException Thrown if there is a problem persisting the user.s
   */
  @Transactional
  public PolysightUser createUser(RawPolysightUser rawUser) throws InsertionException {
    if (findByEmail(rawUser.getEmail()).isPresent()) {
      throw UserAlreadyExistsException.forEmail(rawUser.getEmail());
    }

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

  public PolysightUser deleteUser(long id) throws NotFoundException, DeletionException {
    Optional<PolysightUser> response = userDao.findById(id);
    PolysightUser user =
        response.orElseThrow(() -> NotFoundException.forEntityTypeWithId(PolysightUser.class, id));
    deleteUser(user);
    return user;
  }

  public void deleteUser(PolysightUser user) throws DeletionException {
    userDao.delete(user);
  }

}
