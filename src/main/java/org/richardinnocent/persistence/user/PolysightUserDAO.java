package org.richardinnocent.persistence.user;

import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.persistence.exception.DeletionException;
import org.richardinnocent.persistence.exception.InsertionException;
import org.richardinnocent.persistence.exception.ReadException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public class PolysightUserDAO {

  private final EntityManager entityManager;

  public PolysightUserDAO(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  /**
   * Attempts to find the user with the specified ID.
   * @param id The ID of the user.
   * @return An {@code Optional} containing the user if it was found.
   * @throws ReadException Thrown if there is a problem when attempting to read the user from the
   * database.
   */
  public Optional<PolysightUser> findById(long id) throws ReadException {
    try {
     return Optional.ofNullable(entityManager.find(PolysightUser.class, id));
    } catch (RuntimeException e) {
      throw new ReadException(e);
    }
  }

  /**
   * Attempts to save the user to the database.
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

  /**
   * Attempts to delete the user from the database.
   * @param polysightUser The user to delete.
   * @throws DeletionException Thrown if there is a problem deleting the user.
   */
  public void delete(PolysightUser polysightUser) throws DeletionException {
    try {
      entityManager.remove(polysightUser);
    } catch (RuntimeException e) {
      throw new DeletionException(e);
    }
  }

}
