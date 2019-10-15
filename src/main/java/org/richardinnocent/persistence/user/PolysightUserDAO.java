package org.richardinnocent.persistence.user;

import org.richardinnocent.models.user.PolysightUser;
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

  public void save(PolysightUser polysightUser) {
    entityManager.persist(polysightUser);
  }

}
