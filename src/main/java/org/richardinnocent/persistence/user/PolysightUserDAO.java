package org.richardinnocent.persistence.user;

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
public class PolysightUserDAO extends EntityDAO<PolysightUser> {

  private final PolysightUserRepository userRepo;

  public PolysightUserDAO(PolysightUserRepository userRepo) {
    super(PolysightUser.class);
    this.userRepo = userRepo;
  }

  /**
   * Attempts to find the user with the specified email address.
   * @param email The email of the user.
   * @return The found user, or an empty optional if no user exists with that email address.
   */
  public Optional<PolysightUser> findByEmail(String email) {
    return userRepo.findOne(hasEmail(email));
  }

  private static Specification<PolysightUser> hasEmail(String email) {
    return (user, cq, cb) -> cb.equal(user.get("email"), email);
  }

}
