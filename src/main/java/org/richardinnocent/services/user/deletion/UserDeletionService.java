package org.richardinnocent.services.user.deletion;

import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.persistence.exception.DeletionException;
import org.richardinnocent.persistence.exception.NotFoundException;
import org.richardinnocent.persistence.user.PolysightUserDAO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDeletionService {

  private final PolysightUserDAO userDao;

  public UserDeletionService(PolysightUserDAO userDao) {
    this.userDao = userDao;
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
