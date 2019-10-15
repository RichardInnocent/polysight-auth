package org.richardinnocent.persistence.user;

import org.junit.Test;
import org.richardinnocent.models.user.PolysightUser;

import javax.persistence.EntityManager;

import static org.mockito.Mockito.*;

public class PolysightUserDAOTest {

  private final EntityManager entityManager = mock(EntityManager.class);
  private final PolysightUserDAO dao = new PolysightUserDAO(entityManager);

  @Test
  public void testSave() {
    PolysightUser polysightUser = mock(PolysightUser.class);
    dao.save(polysightUser);
    verify(entityManager).persist(polysightUser);
  }

}