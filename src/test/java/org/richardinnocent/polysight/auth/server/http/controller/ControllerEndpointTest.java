package org.richardinnocent.polysight.auth.server.http.controller;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.KeyPairGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.richardinnocent.polysight.auth.server.http.mapper.HttpObjectMapper;
import org.richardinnocent.polysight.auth.server.persistence.user.PolysightUserDAO;
import org.richardinnocent.polysight.auth.server.persistence.user.UserRoleAssignmentDAO;
import org.richardinnocent.polysight.auth.server.security.JWTPublicPrivateKeyProvider;
import org.richardinnocent.polysight.auth.server.services.user.UserService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

public abstract class ControllerEndpointTest {

  protected static final ObjectMapper MAPPER = new HttpObjectMapper();

  protected MockMvc mvc;

  @MockBean
  protected UserService userService;

  @MockBean
  @SuppressWarnings("unused")
  protected PolysightUserDAO userDao;

  @MockBean
  @SuppressWarnings("unused")
  protected UserRoleAssignmentDAO userRoleAssignmentDAO;

  @MockBean
  @SuppressWarnings("unused")
  protected KeyPairGenerator keyPairGenerator;

  @MockBean
  @SuppressWarnings("unused")
  protected JWTPublicPrivateKeyProvider jwtPublicPrivateKeyProvider;

  @BeforeAll
  public static void setUpMapper() {
    MAPPER.setSerializationInclusion(Include.NON_NULL);
  }

  @BeforeEach
  public void setUpHttpMapper() {
    MappingJackson2HttpMessageConverter messageConverter =
        new MappingJackson2HttpMessageConverter();
    messageConverter.setObjectMapper(MAPPER);

    InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
    viewResolver.setPrefix("/templates");

    mvc = MockMvcBuilders.standaloneSetup(getController())
                         .setMessageConverters(messageConverter)
                         .setViewResolvers(viewResolver)
                         .build();
  }

  protected abstract Object getController();

}
