package org.richardinnocent.services.user.creation;

import org.joda.time.DateTime;
import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.models.user.RawPolysightUser;
import org.richardinnocent.persistence.exception.InsertionException;
import org.richardinnocent.persistence.user.PolysightUserDAO;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserCreationService {

  private final PolysightUserDAO userDao;
  private final PasswordEncoder passwordEncoder;
  private final StringKeyGenerator saltGenerator;

  public UserCreationService(PasswordEncoder encoder,
                             StringKeyGenerator saltGenerator,
                             PolysightUserDAO userDao) {
    this.passwordEncoder = encoder;
    this.saltGenerator = saltGenerator;
    this.userDao = userDao;
  }

  public PolysightUser createUser(RawPolysightUser rawUser) throws InsertionException {
    PolysightUser user = new PolysightUser();
    user.setFullName(rawUser.getFullName());
    user.setEmail(rawUser.getEmail());
    user.setDateOfBirth(rawUser.getDateOfBirth());
    user.setCreationTime(DateTime.now());
    user.setPasswordSalt(saltGenerator.generateKey());
    user.setPassword(passwordEncoder.encode(rawUser.getPassword() + user.getPasswordSalt()));

    userDao.save(user);
    return user;
  }

}
