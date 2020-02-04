package org.richardinnocent.services.user.creation;

import org.joda.time.DateTime;
import org.richardinnocent.models.user.AccountStatus;
import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.models.user.RawPolysightUser;
import org.richardinnocent.persistence.exception.InsertionException;
import org.richardinnocent.persistence.user.PolysightUserDAO;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service used to create new users.
 */
@Service
public class UserCreationService {

  private final PolysightUserDAO userDao;
  private final PasswordEncoder passwordEncoder;
  private final StringKeyGenerator saltGenerator;

  /**
   * Creates a new user creation service.
   * @param encoder The encoder used to encode user's raw passwords.
   * @param saltGenerator The instance used to create password salts.
   * @param userDao The DAO for user entities.
   */
  public UserCreationService(PasswordEncoder encoder,
                             StringKeyGenerator saltGenerator,
                             PolysightUserDAO userDao) {
    this.passwordEncoder = encoder;
    this.saltGenerator = saltGenerator;
    this.userDao = userDao;
  }

  /**
   * Creates a new user from the raw entity type.
   * @param rawUser The raw user entity, straight from the create account page.
   * @return The created user.
   * @throws InsertionException Thrown if there is a problem persisting the user.s
   */
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
    return user;
  }

}
