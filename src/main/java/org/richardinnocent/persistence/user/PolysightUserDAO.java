package org.richardinnocent.persistence.user;

import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.persistence.exception.InsertionException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Transactional
@Repository
public class PolysightUserDAO {

  private final EntityManager entityManager;

  public PolysightUserDAO(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  /**
   * Attemps to save the user to the database.
   * @param polysightUser The user to save.
   * @throws InsertionException Thrown if there is a problem inserting the user.
   */
  public void save(PolysightUser polysightUser) throws InsertionException {
    try {
      entityManager.persist(polysightUser);
    } catch (RuntimeException e) {
      throw new InsertionException(e);
    }
  }

}
