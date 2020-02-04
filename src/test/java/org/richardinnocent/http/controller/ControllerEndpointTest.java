package org.richardinnocent.http.controller;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.richardinnocent.http.mapper.HttpObjectMapper;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

public abstract class ControllerEndpointTest {

  protected static final ObjectMapper MAPPER = new HttpObjectMapper();

  protected MockMvc mvc;

  @BeforeClass
  public static void setUpMapper() {
    MAPPER.setSerializationInclusion(Include.NON_NULL);
  }

  @Before
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
