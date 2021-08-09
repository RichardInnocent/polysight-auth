package org.richardinnocent.polysight.auth.server.persistence;

import java.util.Objects;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.richardinnocent.polysight.auth.server.persistence.exception.DeletionException;
import org.richardinnocent.polysight.auth.server.persistence.exception.InsertionException;
import org.richardinnocent.polysight.auth.server.persistence.exception.ReadException;

public class EntityDAO<T> {

  private final Class<T> type;
  private EntityManager entityManager;

  /**
   * Creates a new entity DAO.
   * @param type The type of entity managed by the DAO.
   */
  public EntityDAO(Class<T> type) {
    this.type = Objects.requireNonNull(type);
  }

  /**
   * Gets the type of entity that is managed by this DAO.
   * @return The type of entity that is managed by this DAO.
   */
  public Class<T> getType() {
    return type;
  }

  @PersistenceContext
  public final void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  /**
   * Gets the entity manager.
   * @return The entity manager.
   */
  protected final EntityManager getEntityManager() {
    return entityManager;
  }

  /**
   * Attempts to find the entity with the specified ID.
   * @param id The ID of the entity.
   * @return An {@code Optional} containing the entity if it was found.
   * @throws ReadException Thrown if there is a problem when attempting to read the entity from the
   * database.
   */
  public Optional<T> findById(long id) {
    try {
      return Optional.ofNullable(getEntityManager().find(type, id));
    } catch (RuntimeException e) {
      throw new ReadException(e);
    }
  }

  /**
   * Attempts to save the entity to the database.
   * @param entity The entity to save.
   * @throws InsertionException Thrown if there is a problem inserting the entity.
   */
  public void save(T entity) throws InsertionException {
    try {
      getEntityManager().persist(entity);
    } catch (RuntimeException e) {
      throw new InsertionException(e);
    }
  }

  /**
   * Attempts to delete the entity from the database.
   * @param entity The entity to delete.
   * @throws DeletionException Thrown if there is a problem deleting the entity.
   */
  public void delete(T entity) throws DeletionException {
    try {
      getEntityManager().remove(entity);
    } catch (RuntimeException e) {
      throw new DeletionException(e);
    }
  }

}
