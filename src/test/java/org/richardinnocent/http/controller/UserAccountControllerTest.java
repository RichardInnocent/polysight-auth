package org.richardinnocent.http.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richardinnocent.http.mapper.HttpObjectMapper;
import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.models.user.RawPolysightUser;
import org.richardinnocent.persistence.exception.DeletionException;
import org.richardinnocent.persistence.exception.NotFoundException;
import org.richardinnocent.services.user.creation.UserCreationService;
import org.richardinnocent.services.user.deletion.UserDeletionService;
import org.richardinnocent.services.user.find.UserSearchService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserAccountController.class)
@SuppressWarnings("unused")
public class UserAccountControllerTest {

  private static final String PREFIX = "/user";
  private static final ObjectMapper MAPPER = new HttpObjectMapper();

  private MockMvc mvc;

  @MockBean
  private UserSearchService searchService;

  @MockBean
  private UserCreationService creationService;

  @MockBean
  private UserDeletionService deletionService;

  @Before
  public void setUpMapper() {
    UserAccountController controller =
        new UserAccountController(searchService, creationService, deletionService);

    MappingJackson2HttpMessageConverter messageConverter =
        new MappingJackson2HttpMessageConverter();
    messageConverter.setObjectMapper(MAPPER);
    mvc = MockMvcBuilders.standaloneSetup(controller)
                         .setMessageConverters(messageConverter)
                         .build();
  }

  @Test
  public void testGetAccount() throws Exception {
    long id = 123L;
    PolysightUser user = createFullyPopulatedPolysightUser(id);

    when(searchService.findById(id)).thenReturn(Optional.of(user));
    mvc.perform(get(PREFIX + "?id=" + id))
       .andExpect(status().isOk())
       .andExpect(content().string(MAPPER.writeValueAsString(user)))
       .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
  }

  @Test
  public void testGetAccountWhenNotFound() throws Exception {
    long id = 123L;
    when(searchService.findById(id)).thenReturn(Optional.empty());
    mvc.perform(get(PREFIX + "?id=" + id))
       .andExpect(status().isOk())
       .andExpect(content().string(""));
  }

  @Test
  public void testCreateAccountAttemptsToCreateAccount() throws Exception {
    RawPolysightUser rawUser = createFullyPopulatedRawUser();
    String account = MAPPER.writeValueAsString(rawUser);

    PolysightUser user = createFullyPopulatedPolysightUser(123L);
    when(creationService.createUser(rawUser)).thenReturn(user);

    postContentToPath(account, "/")
        .andExpect(status().isCreated())
        .andExpect(content().json(MAPPER.writeValueAsString(user)));
    verify(creationService, times(1)).createUser(rawUser);
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

  @Test
  public void testDeleteAccount() throws Exception {
    long id = 123L;
    PolysightUser user = createFullyPopulatedPolysightUser(id);
    when(deletionService.deleteUser(id)).thenReturn(user);
    mvc.perform(delete(PREFIX + "?id=" + id))
       .andExpect(status().isOk())
       .andExpect(content().json(MAPPER.writeValueAsString(user)));
  }

  @Test
  public void testDeleteAccountReturnsBadRequestWhenUserNotFound() throws Exception {
    long id = 123L;
    when(deletionService.deleteUser(id))
        .thenThrow(NotFoundException.forEntityTypeWithId(PolysightUser.class, id));
    mvc.perform(delete(PREFIX + "?id=" + id))
       .andExpect(status().isBadRequest());
  }

  @Test
  public void testDeleteAccountReturnsInternalServerErrorWhenProblemDeleting() throws Exception {
    long id = 123L;
    when(deletionService.deleteUser(id)).thenThrow(new DeletionException());
    mvc.perform(delete(PREFIX + "?id=" + id))
       .andExpect(status().isInternalServerError());
  }

  private PolysightUser createFullyPopulatedPolysightUser(long id) {
    PolysightUser user = new PolysightUser();
    user.setId(id);
    user.setEmail(UserAccountControllerTest.class.getSimpleName() + "@polysighttest.com");
    user.setFullName(UserAccountControllerTest.class.getSimpleName());
    user.setCreationTime(DateTime.now());
    user.setDateOfBirth(new LocalDate(2019, 10, 17));
    user.setPassword("password");
    user.setPasswordSalt("salt");
    return user;
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