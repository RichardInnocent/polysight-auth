package org.richardinnocent.services.user.find;

import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.persistence.exception.ReadException;
import org.richardinnocent.persistence.user.PolysightUserDAO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSearchService {

  private final PolysightUserDAO userDao;

  public UserSearchService(PolysightUserDAO userDao) {
    this.userDao = userDao;
  }

  public Optional<PolysightUser> findById(long id) throws ReadException {
    return userDao.findById(id);
  }

}
