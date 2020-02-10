package org.richardinnocent.persistence.user;

import java.util.List;
import java.util.stream.Collectors;
import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.models.user.UserRole;
import org.richardinnocent.persistence.EntityDAO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * DAO for the application's users.
 */
@Transactional
@Repository
public class PolysightUserDAO extends EntityDAO<PolysightUser> implements UserDetailsService {

  private final PolysightUserRepository userRepo;
  private final UserRoleAssignmentDAO userRoleAssignmentDAO;

  public PolysightUserDAO(PolysightUserRepository userRepo,
                          UserRoleAssignmentDAO userRoleAssignmentDAO) {
    super(PolysightUser.class);
    this.userRepo = userRepo;
    this.userRoleAssignmentDAO = userRoleAssignmentDAO;
  }

  /**
   * Attempts to find the user with the specified email address.
   * @param email The email of the user.
   * @return The found user, or an empty optional if no user exists with that email address.
   */
  public Optional<PolysightUser> findByEmail(String email) {
    return userRepo.findOne(hasEmail(email));
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<PolysightUser> userOptional = findByEmail(email);
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
                                .map(UserRole::getAuthority)
                                .collect(Collectors.toList());
  }

  private static Specification<PolysightUser> hasEmail(String email) {
    return (user, cq, cb) -> cb.equal(user.get("email"), email);
  }

}
