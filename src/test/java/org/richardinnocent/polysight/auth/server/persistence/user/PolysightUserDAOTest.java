package org.richardinnocent.polysight.auth.server.persistence.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.richardinnocent.polysight.auth.server.models.user.PolysightUser;

import javax.persistence.EntityManager;

import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PolysightUserDAOTest {

  private final EntityManager entityManager = mock(EntityManager.class);
  private final PolysightUserRepository userRepo = mock(PolysightUserRepository.class);
  private final PolysightUserDAO dao = new PolysightUserDAO(userRepo);

  @BeforeEach
  public void configureEntityManager() {
    dao.setEntityManager(entityManager);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testFindByEmail() {
    String email = "test@polysight.com";
    PolysightUser returnedUser = mock(PolysightUser.class);
    when(userRepo.findOne(any(Specification.class))).thenReturn(Optional.of(returnedUser));
    Optional<PolysightUser> result = dao.findByEmail(email);
    assertFalse(result.isEmpty());
    assertSame(returnedUser, result.get());
  }

}