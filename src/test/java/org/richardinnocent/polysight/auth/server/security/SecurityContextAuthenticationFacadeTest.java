package org.richardinnocent.polysight.auth.server.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextAuthenticationFacadeTest {

  @Test
  public void testGetAuthentication() {
    SecurityContext context = mock(SecurityContext.class);
    Authentication authentication = mock(Authentication.class);
    when(context.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(context);
    assertEquals(authentication, new SecurityContextAuthenticationFacade().getAuthentication());
  }

  @Test
  public void testSetAuthentication() {
    SecurityContext context = mock(SecurityContext.class);
    SecurityContextHolder.setContext(context);
    Authentication authentication = mock(Authentication.class);
    new SecurityContextAuthenticationFacade().setAuthentication(authentication);
    verify(context, times(1)).setAuthentication(authentication);
  }

}