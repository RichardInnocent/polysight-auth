package org.richardinnocent.services.user.creation;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.models.user.RawPolysightUser;
import org.richardinnocent.persistence.user.PolysightUserDAO;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserCreationServiceTest {

  private final PolysightUserDAO userDao = mock(PolysightUserDAO.class);
  private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
  private final StringKeyGenerator saltGenerator = mock(StringKeyGenerator.class);
  private final UserCreationService userCreationService =
      new UserCreationService(passwordEncoder, saltGenerator, userDao);

  @Test
  public void testCreateUser() {
    RawPolysightUser rawUser = new RawPolysightUser();
    rawUser.setFullName("Test name");
    rawUser.setEmail("test@polysight.org");
    rawUser.setDateOfBirth(new LocalDate("2000-07-13"));
    rawUser.setPassword("some password");

    String salt = "generated salt";
    String encryptedPassword = "encrypted password";
    when(saltGenerator.generateKey()).thenReturn(salt);
    when(passwordEncoder.encode(any(CharSequence.class))).thenReturn(encryptedPassword);

    PolysightUser savedUser = userCreationService.createUser(rawUser);
    verify(saltGenerator, times(1)).generateKey();
    verify(passwordEncoder, times(1)).encode(rawUser.getPassword() + salt);
    verify(userDao, times(1)).save(savedUser);

    assertEquals(rawUser.getFullName(), savedUser.getFullName());
    assertEquals(rawUser.getEmail(), savedUser.getEmail());
    assertEquals(rawUser.getDateOfBirth(), savedUser.getDateOfBirth());
    assertEquals(encryptedPassword, savedUser.getPassword());
    assertEquals(salt, savedUser.getPasswordSalt());
    assertTrue(savedUser.getCreationTime().isBeforeNow());
  }

}