package org.richardinnocent.polysight.auth.server.http.controller.api.v1.users;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;
import org.richardinnocent.polysight.auth.server.models.user.AccountStatus;
import org.richardinnocent.polysight.auth.server.models.user.PolysightUser;

class UserDtoTest {

  @Test
  public void equalsAndHashcode_Always_Valid() {
    EqualsVerifier.forClass(UserDto.class).suppress(Warning.STRICT_INHERITANCE).verify();
  }

  @Test
  public void constructor$PolysightUser_UserIsPresent_DtoCreatedAsExpected() {
    PolysightUser user = new PolysightUser();
    user.setId(25L);
    user.setEmail("test@user.com");
    user.setFirstName("Test");
    user.setLastName("User");
    user.setDateOfBirth(LocalDate.of(2021, 8, 9));
    user.setAccountStatus(AccountStatus.ACTIVE);

    UserDto dto = UserDto.forUser(user);
    assertEquals(user.getId(), dto.getId());
    assertEquals(user.getEmail(), dto.getEmail());
    assertEquals(user.getFirstName(), dto.getFirstName());
    assertEquals(user.getLastName(), dto.getLastName());
    assertEquals(user.getAccountStatus(), dto.getAccountStatus());
    assertEquals(user.getDateOfBirth(), dto.getDateOfBirth());
  }

}