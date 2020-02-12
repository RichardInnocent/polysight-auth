package org.richardinnocent.services.user.find;

import java.util.List;
import java.util.stream.Collectors;
import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.models.user.UserRole;
import org.richardinnocent.models.user.UserRoleAssignment;
import org.richardinnocent.persistence.exception.ReadException;
import org.richardinnocent.persistence.user.PolysightUserDAO;
import org.richardinnocent.persistence.user.UserRoleAssignmentDAO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSearchService implements UserDetailsService {

  private final PolysightUserDAO userDao;
  private final UserRoleAssignmentDAO userRoleAssignmentDAO;

  public UserSearchService(PolysightUserDAO userDao,
                           UserRoleAssignmentDAO userRoleAssignmentDAO) {
    this.userDao = userDao;
    this.userRoleAssignmentDAO = userRoleAssignmentDAO;
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

}
