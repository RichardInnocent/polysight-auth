package org.richardinnocent.polysight.auth.server.persistence;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.richardinnocent.polysight.auth.server.persistence.exception.DeletionException;
import org.richardinnocent.polysight.auth.server.persistence.exception.InsertionException;
import org.richardinnocent.polysight.auth.server.persistence.exception.ReadException;

public class EntityDAOTest {

  private final EntityManager entityManager = mock(EntityManager.class);
  private final EntityDAO<TestEntity> dao = new EntityDAO<>(TestEntity.class);

  @Before
  public void configureEntityManager() {
    dao.setEntityManager(entityManager);
  }

  @Test(expected = NullPointerException.class)
  public void testTypeCannotBeNull() {
    new EntityDAO<>(null);
  }

  @Test
  public void testTypeIsSet() {
    Class<TestEntity> entityType = TestEntity.class;
    assertEquals(entityType, new EntityDAO<>(entityType).getType());
  }

  @Test
  public void testGetWhenEntityFound() {
    long id = 123L;
    TestEntity entity = mock(TestEntity.class);
    when(entityManager.find(TestEntity.class, id)).thenReturn(entity);
    Optional<TestEntity> result = dao.findById(id);
    assertTrue("User is empty", result.isPresent());
    assertEquals(entity, result.get());
  }

  @Test
  public void testGetWhenEntityNotFound() {
    assertTrue(dao.findById(123L).isEmpty());
  }

  @Test(expected = ReadException.class)
  public void testGetWhenExceptionIsThrown() {
    when(entityManager.find(eq(TestEntity.class), anyLong())).thenThrow(new RuntimeException());
    dao.findById(123L);
  }

  @Test
  public void testSave() {
    TestEntity entity = mock(TestEntity.class);
    dao.save(entity);
    verify(entityManager).persist(entity);
  }

  @Test(expected = InsertionException.class)
  public void testSaveWhenAnExceptionIsThrown() {
    doThrow(new RuntimeException()).when(entityManager).persist(any(Object.class));
    dao.save(mock(TestEntity.class));
  }

  @Test
  public void testDelete() {
    TestEntity entity = mock(TestEntity.class);
    dao.delete(entity);
    verify(entityManager).remove(entity);
  }

  @Test(expected = DeletionException.class)
  public void testDeleteWhenExceptionIsThrown() {
    doThrow(new RuntimeException()).when(entityManager).remove(any(Object.class));
    dao.delete(mock(TestEntity.class));
  }

  private static class TestEntity {}

}