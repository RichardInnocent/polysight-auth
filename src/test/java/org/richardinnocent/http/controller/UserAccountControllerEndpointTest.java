package org.richardinnocent.http.controller;

import java.util.Map;
import java.util.stream.IntStream;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.richardinnocent.models.user.PolysightUser;
import org.richardinnocent.models.user.RawPolysightUser;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.web.util.UriUtils;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("UNIT_TEST")
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
@SuppressWarnings("unused")
public class UserAccountControllerEndpointTest extends ControllerEndpointTest {

  public Object getController() {
    return new UserAccountController(userService);
  }

  @Test
  public void testGetSignupPage() throws Exception {
    mvc.perform(get("/signup"))
       .andExpect(status().isOk());
  }

  @Test
  public void testCreateAccountAttemptsToCreateAccount() throws Exception {
    RawPolysightUser rawUser = createFullyPopulatedRawUser();
    String account = toFormData(rawUser);

    PolysightUser user = createFullyPopulatedPolysightUser(123L);
    when(userService.createUser(rawUser)).thenReturn(user);

    postToPath(account, "/signup", MediaType.APPLICATION_FORM_URLENCODED)
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string("Location", "/login"));
    verify(userService, times(1)).createUser(eq(rawUser));
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
  public void testCreateAccountWithEmailLongerThan128CharsIsBadRequest() throws Exception {
    RawPolysightUser user = createFullyPopulatedRawUser();
    user.setEmail(createStringOfLength(129));
    postToPath(toFormData(user), "/signup", MediaType.APPLICATION_FORM_URLENCODED)
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateAccountWithNoFirstNameIsBadRequest() throws Exception {
    RawPolysightUser rawUser = createFullyPopulatedRawUser();
    rawUser.setFirstName(null);
    postToPath(toFormData(rawUser), "/signup", MediaType.APPLICATION_FORM_URLENCODED)
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateAccountWithEmptyFirstNameIsBadRequest() throws Exception {
    RawPolysightUser rawUser = createFullyPopulatedRawUser();
    rawUser.setFirstName("");
    postToPath(toFormData(rawUser), "/signup", MediaType.APPLICATION_FORM_URLENCODED)
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateAccountWithFirstNameLongerThan32CharsIsBadRequest() throws Exception {
    RawPolysightUser rawUser = createFullyPopulatedRawUser();
    rawUser.setFirstName(createStringOfLength(33));
    postToPath(toFormData(rawUser), "/signup", MediaType.APPLICATION_FORM_URLENCODED)
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateAccountWithNoLastNameIsBadRequest() throws Exception {
    RawPolysightUser rawUser = createFullyPopulatedRawUser();
    rawUser.setLastName(null);
    postToPath(toFormData(rawUser), "/signup", MediaType.APPLICATION_FORM_URLENCODED)
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateAccountWithEmptyLastNameIsBadRequest() throws Exception {
    RawPolysightUser rawUser = createFullyPopulatedRawUser();
    rawUser.setLastName("");
    postToPath(toFormData(rawUser), "/signup", MediaType.APPLICATION_FORM_URLENCODED)
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateAccountWithLastNameLongerThan32CharsIsBadRequest() throws Exception {
    RawPolysightUser rawUser = createFullyPopulatedRawUser();
    rawUser.setLastName(createStringOfLength(33));
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

  @Test
  public void testGetProfilePage() throws Exception {
    mvc.perform(get("/profile"))
       .andExpect(status().isOk());
  }

  private PolysightUser createFullyPopulatedPolysightUser(long id) {
    PolysightUser user = new PolysightUser();
    user.setId(id);
    user.setEmail(UserAccountControllerEndpointTest.class.getSimpleName() + "@polysighttest.com");
    user.setFirstName(UserAccountControllerEndpointTest.class.getSimpleName());
    user.setCreationTime(DateTime.now());
    user.setDateOfBirth(new LocalDate(2019, 10, 17));
    user.setPassword("password");
    user.setPasswordSalt("salt");
    return user;
  }

  private RawPolysightUser createFullyPopulatedRawUser() {
    RawPolysightUser rawUser = new RawPolysightUser();
    rawUser.setEmail("test@polysight.org");
    rawUser.setFirstName("Forename");
    rawUser.setLastName("Surname");
    rawUser.setDateOfBirth(new LocalDate("1990-04-03"));
    rawUser.setPassword("password01");
    return rawUser;
  }

  private String createStringOfLength(int length) {
    return IntStream.range(0, length)
                    .mapToObj(i -> (char) (i + 'a'))
                    .reduce("",
                            (text, character) -> text + character,
                            (text1, text2) -> text1 + text2);
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