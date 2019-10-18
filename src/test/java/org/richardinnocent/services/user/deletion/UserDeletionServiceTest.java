package org.richardinnocent.services.user.deletion;

import org.junit.Test;
import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.persistence.exception.NotFoundException;
import org.richardinnocent.persistence.user.PolysightUserDAO;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class UserDeletionServiceTest {

  private final PolysightUserDAO userDao = mock(PolysightUserDAO.class);
  private final UserDeletionService service = new UserDeletionService(userDao);

  @Test
  public void testDeleteUser() {
    PolysightUser user = mock(PolysightUser.class);
    service.deleteUser(user);
    verify(userDao, times(1)).delete(user);
  }

  @Test
  public void testDeleteUserById() {
    long id = 123L;
    PolysightUser user = mock(PolysightUser.class);
    when(userDao.findById(id)).thenReturn(Optional.of(user));
    service.deleteUser(id);
    verify(userDao, times(1)).delete(user);
  }

  @Test(expected = NotFoundException.class)
  public void testDeleteUserByIdWhenNotFound() {
    when(userDao.findById(anyLong())).thenReturn(Optional.empty());
    service.deleteUser(123L);
  }

}