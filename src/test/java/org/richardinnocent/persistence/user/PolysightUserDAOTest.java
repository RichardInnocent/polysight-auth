package org.richardinnocent.persistence.user;

import org.junit.Test;
import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.persistence.exception.DeletionException;
import org.richardinnocent.persistence.exception.InsertionException;
import org.richardinnocent.persistence.exception.ReadException;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PolysightUserDAOTest {

  private final EntityManager entityManager = mock(EntityManager.class);
  private final PolysightUserDAO dao = new PolysightUserDAO(entityManager);

  @Test
  public void testGetWhenEntityFound() {
    long id = 123L;
    PolysightUser user = mock(PolysightUser.class);
    when(entityManager.find(PolysightUser.class, id)).thenReturn(user);
    Optional<PolysightUser> result = dao.findById(id);
    assertTrue("User is empty", result.isPresent());
    assertEquals(user, result.get());
  }

  @Test
  public void testGetWhenEntityNotFound() {
    assertTrue(dao.findById(123L).isEmpty());
  }

  @Test(expected = ReadException.class)
  public void testGetWhenExceptionIsThrown() {
    when(entityManager.find(eq(PolysightUser.class), anyLong())).thenThrow(new RuntimeException());
    dao.findById(123L);
  }

  @Test
  public void testSave() {
    PolysightUser user = mock(PolysightUser.class);
    dao.save(user);
    verify(entityManager).persist(user);
  }

  @Test(expected = InsertionException.class)
  public void testSaveWhenAnExceptionIsThrown() {
    doThrow(new RuntimeException()).when(entityManager).persist(any(Object.class));
    dao.save(mock(PolysightUser.class));
  }

  @Test
  public void testDelete() {
    PolysightUser user = mock(PolysightUser.class);
    dao.delete(user);
    verify(entityManager).remove(user);
  }

  @Test(expected = DeletionException.class)
  public void testDeleteWhenExceptionIsThrown() {
    doThrow(new RuntimeException()).when(entityManager).remove(any(Object.class));
    dao.delete(mock(PolysightUser.class));
  }

}