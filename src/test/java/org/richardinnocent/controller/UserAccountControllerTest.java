package org.richardinnocent.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserAccountController.class)
@SuppressWarnings("unused")
public class UserAccountControllerTest {

  private static final String PREFIX = "/user";

  @Autowired
  private MockMvc mvc;

  @Test
  public void testCreateAccountIsNotImplemented() throws Exception {
    mvc.perform(post(PREFIX)).andExpect(status().isNotImplemented());
  }

  @Test
  public void testGetAccountIsNotImplemented() throws Exception {
    mvc.perform(get(PREFIX)).andExpect(status().isNotImplemented());
  }

  @Test
  public void testLoginIsNotImplemented() throws Exception {
    mvc.perform(post(PREFIX + "/login")).andExpect(status().isNotImplemented());
  }

  @Test
  public void testValidateIsNotImplemented() throws Exception {
    mvc.perform(post(PREFIX + "/validate")).andExpect(status().isNotImplemented());
  }

}