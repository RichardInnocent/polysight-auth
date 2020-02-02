package org.richardinnocent.http.controller;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richardinnocent.http.mapper.HttpObjectMapper;
import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.models.user.RawPolysightUser;
import org.richardinnocent.services.user.creation.UserCreationService;
import org.richardinnocent.services.user.deletion.UserDeletionService;
import org.richardinnocent.services.user.find.UserSearchService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriUtils;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@SuppressWarnings("unused")
public class UserAccountControllerTest {

  private static final ObjectMapper MAPPER = new HttpObjectMapper();

  private MockMvc mvc;

  @MockBean
  private UserSearchService searchService;

  @MockBean
  private UserCreationService creationService;

  @MockBean
  private UserDeletionService deletionService;

  @BeforeClass
  public static void setUpMapper() {
    MAPPER.setSerializationInclusion(Include.NON_NULL);
  }

  @Before
  public void setUpHttpMapper() {
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
  public void testCreateAccountAttemptsToCreateAccount() throws Exception {
    RawPolysightUser rawUser = createFullyPopulatedRawUser();
    String account = toFormData(rawUser);

    PolysightUser user = createFullyPopulatedPolysightUser(123L);
    when(creationService.createUser(rawUser)).thenReturn(user);

    postToPath(account, "/signup", MediaType.APPLICATION_FORM_URLENCODED)
        .andExpect(status().isOk());
    verify(creationService, times(1)).createUser(eq(rawUser));
  }

  @Test
  public void testCreateAccountWithNoEmailIsBadRequest() throws Exception {
    RawPolysightUser rawUser = createFullyPopulatedRawUser();
    rawUser.setEmail(null);
    postToPath(toFormData(rawUser), "/signup", MediaType.APPLICATION_FORM_URLENCODED)
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateAccountWithEmailShorterThan3CharactersIsBadRequest() throws Exception {
    RawPolysightUser rawUser = createFullyPopulatedRawUser();
    rawUser.setEmail("NA");
    postToPath(toFormData(rawUser), "/signup", MediaType.APPLICATION_FORM_URLENCODED)
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateAccountWithNoNameIsBadRequest() throws Exception {
    RawPolysightUser rawUser = createFullyPopulatedRawUser();
    rawUser.setFullName(null);
    postToPath(toFormData(rawUser), "/signup", MediaType.APPLICATION_FORM_URLENCODED)
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateAccountWithNoDateOfBirthIsBadRequest() throws Exception {
    RawPolysightUser rawUser = createFullyPopulatedRawUser();
    rawUser.setDateOfBirth(null);
    postToPath(toFormData(rawUser), "/signup", MediaType.APPLICATION_FORM_URLENCODED)
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateAccountWithNoPasswordIsBadRequest() throws Exception {
    RawPolysightUser rawUser = createFullyPopulatedRawUser();
    rawUser.setPassword(null);
    postToPath(toFormData(rawUser), "/signup", MediaType.APPLICATION_FORM_URLENCODED)
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateAccountWithPasswordShortedThan8CharactersIsBadRequest() throws Exception {
    RawPolysightUser rawUser = createFullyPopulatedRawUser();
    rawUser.setPassword("7 chars");
    postToPath(toFormData(rawUser), "/signup", MediaType.APPLICATION_FORM_URLENCODED)
        .andExpect(status().isBadRequest());
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

  @SuppressWarnings("unchecked")
  private String toFormData(RawPolysightUser user) {
    StringBuilder formData = new StringBuilder();
    Map<String, Object> userMap = MAPPER.convertValue(user, Map.class);

    userMap.forEach(
        (key, value) ->
            formData.append(key)
                    .append('=')
                    .append(UriUtils.encode(value.toString(), "UTF-8"))
                    .append('&'));

    return formData.length() > 0 ?
        formData.substring(0, formData.length() - 1) : formData.toString();
  }

  private ResultActions postToPath(String formData, String path, MediaType mediaType)
      throws Exception {
    return mvc.perform(post(path).content(formData).contentType(mediaType));
  }

}