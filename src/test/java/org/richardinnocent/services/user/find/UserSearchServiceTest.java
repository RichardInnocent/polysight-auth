package org.richardinnocent.services.user.find;

import org.junit.Test;
import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.persistence.user.PolysightUserDAO;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserSearchServiceTest {

  private final PolysightUserDAO userDao = mock(PolysightUserDAO.class);
  private final UserSearchService service = new UserSearchService(userDao);

  @Test
  public void findById() {
    Optional<PolysightUser> result = Optional.of(mock(PolysightUser.class));
    long id = 123L;
    when(userDao.findById(id)).thenReturn(result);
    assertEquals(result, service.findById(id));
  }

}