package org.richardinnocent.security;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.io.IOException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

public class JWTAuthorizationFilterTest {

  private static PublicPrivateKeyProvider keyProvider;

  private final AuthenticationManager authManager = mock(AuthenticationManager.class);
  private final SecurityContext securityContext = mock(SecurityContext.class);
  private final HttpServletRequest request = mock(HttpServletRequest.class);
  private final HttpServletResponse response = mock(HttpServletResponse.class);
  private final FilterChain chain = mock(FilterChain.class);

  private final JWTAuthorizationFilter filter =
      new JWTAuthorizationFilter(authManager, keyProvider, securityContext);

  @BeforeClass
  public static void setUpKeyProvider() throws NoSuchAlgorithmException {
    keyProvider = new JWTPublicPrivateKeyProvider(KeyPairGenerator.getInstance("EC"));
  }

  @Test
  public void checkAuthenticationNotSetIfCookiesAreNull() throws IOException, ServletException {
    when(request.getCookies()).thenReturn(null);
    filter.doFilterInternal(request, response, chain);
    verify(securityContext, never()).setAuthentication(any());
    verifyChainContinued();
  }

  @Test
  public void checkAuthenticationNotSetIfThereAreNoCookies() throws IOException, ServletException {
    when(request.getCookies()).thenReturn(new Cookie[]{});
    filter.doFilterInternal(request, response, chain);
    verify(securityContext, never()).setAuthentication(any());
    verifyChainContinued();
  }

  @Test
  public void checkAuthenticationNotSetIfThereIsNoPolysightAuthCookie()
      throws IOException, ServletException {
    Cookie cookie1 = mock(Cookie.class);
    when(cookie1.getName()).thenReturn("Not a Polysight cookie");
    Cookie cookie2 = mock(Cookie.class);
    when(cookie2.getName()).thenReturn("Not a Polysight cookie");
    Cookie cookie3 = mock(Cookie.class);
    when(cookie3.getName()).thenReturn("Not a Polysight cookie");

    when(request.getCookies()).thenReturn(new Cookie[]{cookie1, cookie2, cookie3});
    filter.doFilterInternal(request, response, chain);
    verify(securityContext, never()).setAuthentication(any());
    verifyChainContinued();
  }

  @Test
  public void testEmptyTokenCookieSetsContextToNull() throws IOException, ServletException {
    Cookie cookie = mock(Cookie.class);
    when(cookie.getName()).thenReturn(JWTCookieFields.COOKIE_NAME);

    when(request.getCookies()).thenReturn(new Cookie[]{cookie});
    filter.doFilterInternal(request, response, chain);
    verify(securityContext, times(1)).setAuthentication(null);
    verifyChainContinued();
  }

  @Test
  public void testInvalidJwtCookieThenNullIsAppliedToTheSecurityContext()
      throws IOException, ServletException {
    Cookie cookie = mock(Cookie.class);
    when(cookie.getName()).thenReturn(JWTCookieFields.COOKIE_NAME);
    when(cookie.getValue()).thenReturn("not a valid token");
    when(request.getCookies()).thenReturn(new Cookie[]{cookie});

    filter.doFilterInternal(request, response, chain);

    verify(securityContext, times(1)).setAuthentication(null);
    verifyChainContinued();
  }

  @Test
  public void testValidJwtCookieIsAppliedToSecurityContext() throws IOException, ServletException {
    String email = "test@polysight.com";
    String token =
        JWT.create()
           .withIssuer(JWTCookieFields.ISSUER)
           .withClaim(JWTCookieFields.EMAIL_CLAIM_KEY, email)
           .withExpiresAt(new Date(System.currentTimeMillis() + 10_000L))
           .sign(Algorithm.ECDSA512((ECPublicKey) keyProvider.getPublicKey(),
                                    (ECPrivateKey) keyProvider.getPrivateKey()));

    Cookie cookie = mock(Cookie.class);
    when(cookie.getName()).thenReturn(JWTCookieFields.COOKIE_NAME);
    when(cookie.getValue()).thenReturn(token);
    when(request.getCookies()).thenReturn(new Cookie[]{cookie});

    ArgumentCaptor<Authentication> authenticationCaptor =
        ArgumentCaptor.forClass(Authentication.class);

    filter.doFilterInternal(request, response, chain);

    verify(securityContext, times(1))
        .setAuthentication(authenticationCaptor.capture());

    assertEquals(email, authenticationCaptor.getValue().getPrincipal());
    verifyChainContinued();
  }

  @Test
  public void testIfJwtCookieIsValidButHasNoEmailItIsNotAppliedToSecurityContext()
      throws IOException, ServletException {
    String token =
        JWT.create()
           .withIssuer(JWTCookieFields.ISSUER)
           .withExpiresAt(new Date(System.currentTimeMillis() + 10_000L))
           .sign(Algorithm.ECDSA512((ECPublicKey) keyProvider.getPublicKey(),
                                    (ECPrivateKey) keyProvider.getPrivateKey()));

    Cookie cookie = mock(Cookie.class);
    when(cookie.getName()).thenReturn(JWTCookieFields.COOKIE_NAME);
    when(cookie.getValue()).thenReturn(token);
    when(request.getCookies()).thenReturn(new Cookie[]{cookie});

    filter.doFilterInternal(request, response, chain);

    verify(securityContext, times(1)).setAuthentication(null);
    verifyChainContinued();
  }

  private void verifyChainContinued() throws IOException, ServletException {
    verify(chain, times(1)).doFilter(request, response);
  }


}