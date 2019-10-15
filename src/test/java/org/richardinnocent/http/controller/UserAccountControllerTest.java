package org.richardinnocent.http.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.joda.time.LocalDate;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.models.user.RawPolysightUser;
import org.richardinnocent.services.user.creation.UserCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserAccountController.class)
@SuppressWarnings("unused")
public class UserAccountControllerTest {

  private static final String PREFIX = "/user";
  private static final ObjectMapper MAPPER = new ObjectMapper();

  @Autowired
  private MockMvc mvc;

  @MockBean
  private UserCreationService userCreationService;

  @BeforeClass
  public static void setUpMapper() {
    MAPPER.registerModule(new JodaModule());
  }

  @Test
  public void testCreateAccountIsNotImplemented() throws Exception {
    RawPolysightUser rawUser = createFullyPopulatedRawUser();
    String account = MAPPER.writeValueAsString(rawUser);

    when(userCreationService.createUser(rawUser)).thenReturn(mock(PolysightUser.class));

    postContentToPath(account, "/").andExpect(status().isCreated());
    verify(userCreationService, times(1)).createUser(rawUser);
  }

  @Test
  public void testCreateAccountWithNoEmailIsBadRequest() throws Exception {
    RawPolysightUser rawUser = createFullyPopulatedRawUser();
    rawUser.setEmail(null);
    postContentToPath(MAPPER.writeValueAsString(rawUser), "/")
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateAccountWithNoNameIsBadRequest() throws Exception {
    RawPolysightUser rawUser = createFullyPopulatedRawUser();
    rawUser.setFullName(null);
    postContentToPath(MAPPER.writeValueAsString(rawUser), "/")
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateAccountWithNoDateOfBirthIsBadRequest() throws Exception {
    RawPolysightUser rawUser = createFullyPopulatedRawUser();
    rawUser.setDateOfBirth(null);
    postContentToPath(MAPPER.writeValueAsString(rawUser), "/")
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateAccountWithNoPasswordIsBadRequest() throws Exception {
    RawPolysightUser rawUser = createFullyPopulatedRawUser();
    rawUser.setPassword(null);
    postContentToPath(MAPPER.writeValueAsString(rawUser), "/")
        .andExpect(status().isBadRequest());
  }


  private RawPolysightUser createFullyPopulatedRawUser() {
    RawPolysightUser rawUser = new RawPolysightUser();
    rawUser.setEmail("test@polysight.org");
    rawUser.setFullName("Test Polysight User");
    rawUser.setDateOfBirth(new LocalDate("1990-04-03"));
    rawUser.setPassword("password01");
    return rawUser;
  }

  @Test
  public void testGetAccountIsNotImplemented() throws Exception {
    mvc.perform(get(PREFIX)).andExpect(status().isNotImplemented());
  }

  @Test
  public void testLoginIsNotImplemented() throws Exception {
    postContentToPath("", "/login").andExpect(status().isNotImplemented());
  }

  @Test
  public void testValidateIsNotImplemented() throws Exception {
    postContentToPath("", "/validate").andExpect(status().isNotImplemented());
  }

  private ResultActions postContentToPath(String content, String path) throws Exception {
    return mvc.perform(post(PREFIX + path).content(content).contentType(MediaType.APPLICATION_JSON));
  }

}