package org.richardinnocent.polysight.auth.server.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.io.IOException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Collection;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class JWTAuthorizationFilterTest {

  private static PublicPrivateKeyProvider keyProvider;

  private final AuthenticationManager authManager = mock(AuthenticationManager.class);
  private final AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
  private final HttpServletRequest request = mock(HttpServletRequest.class);
  private final HttpServletResponse response = mock(HttpServletResponse.class);
  private final FilterChain chain = mock(FilterChain.class);

  private final JWTAuthorizationFilter filter =
      new JWTAuthorizationFilter(authManager, keyProvider, authenticationFacade);

  @BeforeAll
  public static void setUpKeyProvider() throws NoSuchAlgorithmException {
    keyProvider = new JWTPublicPrivateKeyProvider(KeyPairGenerator.getInstance("EC"));
  }

  @Test
  public void checkAuthenticationNotSetIfCookiesAreNull() throws IOException, ServletException {
    when(request.getCookies()).thenReturn(null);
    filter.doFilterInternal(request, response, chain);
    verify(authenticationFacade, never()).setAuthentication(any());
    verifyChainContinued();
  }

  @Test
  public void checkAuthenticationNotSetIfThereAreNoCookies() throws IOException, ServletException {
    when(request.getCookies()).thenReturn(new Cookie[]{});
    filter.doFilterInternal(request, response, chain);
    verify(authenticationFacade, never()).setAuthentication(any());
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
    verify(authenticationFacade, never()).setAuthentication(any());
    verifyChainContinued();
  }

  @Test
  public void testEmptyTokenCookieDoesNotSetContext() throws IOException, ServletException {
    Cookie cookie = mock(Cookie.class);
    when(cookie.getName()).thenReturn(JWTCookieFields.COOKIE_NAME);

    when(request.getCookies()).thenReturn(new Cookie[]{cookie});
    filter.doFilterInternal(request, response, chain);
    verify(authenticationFacade, never()).setAuthentication(any());
    verifyChainContinued();
  }

  @Test
  public void testInvalidJwtCookieDoesNotSetContext()
      throws IOException, ServletException {
    Cookie cookie = mock(Cookie.class);
    when(cookie.getName()).thenReturn(JWTCookieFields.COOKIE_NAME);
    when(cookie.getValue()).thenReturn("not a valid token");
    when(request.getCookies()).thenReturn(new Cookie[]{cookie});

    filter.doFilterInternal(request, response, chain);

    verify(authenticationFacade, never()).setAuthentication(any());
    verifyChainContinued();
  }

  @Test
  public void testValidJwtCookieIsAppliedToSecurityContext() throws IOException, ServletException {
    String email = "test@polysight.com";
    String authority1 = "authority1";
    String authority2 = "authority2";
    String authority3 = "authority3";
    String authorities = "[\"" + authority1 + "\",\"" + authority2 + "\",\"" + authority3 + "\"]";
    String token =
        JWT.create()
           .withIssuer(JWTCookieFields.ISSUER)
           .withClaim(JWTCookieFields.EMAIL_CLAIM_KEY, email)
           .withClaim(JWTCookieFields.AUTHORITIES_CLAIM_KEY, authorities)
           .withExpiresAt(new Date(System.currentTimeMillis() + 10_000L))
           .sign(Algorithm.ECDSA512((ECPublicKey) keyProvider.getPublicKey(),
                                    (ECPrivateKey) keyProvider.getPrivateKey()));

    Cookie cookie = mock(Cookie.class);
    when(cookie.getName()).thenReturn(JWTCookieFields.COOKIE_NAME);
    when(cookie.getValue()).thenReturn(token);
    when(request.getCookies()).thenReturn(new Cookie[]{cookie});
    when(request.getContextPath()).thenReturn("contextPath");
    when(request.getRequestURI()).thenReturn("contextPath/profile");

    ArgumentCaptor<Authentication> authenticationCaptor =
        ArgumentCaptor.forClass(Authentication.class);

    filter.doFilterInternal(request, response, chain);

    verify(authenticationFacade, times(1))
        .setAuthentication(authenticationCaptor.capture());

    assertEquals(email, authenticationCaptor.getValue().getPrincipal());
    Collection<? extends GrantedAuthority> detectedAuthorities =
        authenticationCaptor.getValue().getAuthorities();
    assertTrue(
        detectedAuthorities.stream().allMatch(auth -> authorities.contains(auth.getAuthority())));
    assertEquals(3, detectedAuthorities.size());
    verifyChainContinued();
  }

  @Test
  public void testValidCookieIsAppliedToSecurityContextButIsRedirectedIfAccessingLoginPage()
    throws IOException, ServletException {
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
    when(request.getContextPath()).thenReturn("contextPath");
    when(request.getRequestURI()).thenReturn("contextPath/login?hasParameters");
    when(request.getCookies()).thenReturn(new Cookie[]{cookie});

    ArgumentCaptor<Authentication> authenticationCaptor =
        ArgumentCaptor.forClass(Authentication.class);

    filter.doFilterInternal(request, response, chain);

    verify(authenticationFacade, times(1))
        .setAuthentication(authenticationCaptor.capture());

    assertEquals(email, authenticationCaptor.getValue().getPrincipal());
    verify(response, times(1)).sendRedirect("/profile");
    verifyChainNotContinued();
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

    verify(authenticationFacade, never()).setAuthentication(any());
    verifyChainContinued();
  }

  @Test
  public void testIFJwtIsValidButHasNoRolesThenEmptyRolesAreAssignedToSecurityContext()
      throws IOException, ServletException {
    String email = "test@polysight.com";
    String token =
        JWT.create()
           .withIssuer(JWTCookieFields.ISSUER)
           .withClaim(JWTCookieFields.EMAIL_CLAIM_KEY, email)
           .withClaim(JWTCookieFields.AUTHORITIES_CLAIM_KEY, (String) null)
           .withExpiresAt(new Date(System.currentTimeMillis() + 10_000L))
           .sign(Algorithm.ECDSA512((ECPublicKey) keyProvider.getPublicKey(),
                                    (ECPrivateKey) keyProvider.getPrivateKey()));

    Cookie cookie = mock(Cookie.class);
    when(cookie.getName()).thenReturn(JWTCookieFields.COOKIE_NAME);
    when(cookie.getValue()).thenReturn(token);
    when(request.getCookies()).thenReturn(new Cookie[]{cookie});
    when(request.getContextPath()).thenReturn("contextPath");
    when(request.getRequestURI()).thenReturn("contextPath/profile");

    ArgumentCaptor<Authentication> authenticationCaptor =
        ArgumentCaptor.forClass(Authentication.class);

    filter.doFilterInternal(request, response, chain);

    verify(authenticationFacade, times(1))
        .setAuthentication(authenticationCaptor.capture());

    assertEquals(email, authenticationCaptor.getValue().getPrincipal());
    assertTrue(authenticationCaptor.getValue().getAuthorities().isEmpty());
    verifyChainContinued();
  }

  @Test
  public void testIFJwtIsValidButHasUnparsableRolesThenEmptyRolesAreAssignedToSecurityContext()
      throws IOException, ServletException {
    String email = "test@polysight.com";
    String token =
        JWT.create()
           .withIssuer(JWTCookieFields.ISSUER)
           .withClaim(JWTCookieFields.EMAIL_CLAIM_KEY, email)
           .withClaim(JWTCookieFields.AUTHORITIES_CLAIM_KEY, "not a parsable array")
           .withExpiresAt(new Date(System.currentTimeMillis() + 10_000L))
           .sign(Algorithm.ECDSA512((ECPublicKey) keyProvider.getPublicKey(),
                                    (ECPrivateKey) keyProvider.getPrivateKey()));

    Cookie cookie = mock(Cookie.class);
    when(cookie.getName()).thenReturn(JWTCookieFields.COOKIE_NAME);
    when(cookie.getValue()).thenReturn(token);
    when(request.getCookies()).thenReturn(new Cookie[]{cookie});
    when(request.getContextPath()).thenReturn("contextPath");
    when(request.getRequestURI()).thenReturn("contextPath/profile");

    ArgumentCaptor<Authentication> authenticationCaptor =
        ArgumentCaptor.forClass(Authentication.class);

    filter.doFilterInternal(request, response, chain);

    verify(authenticationFacade, times(1))
        .setAuthentication(authenticationCaptor.capture());

    assertEquals(email, authenticationCaptor.getValue().getPrincipal());
    assertTrue(authenticationCaptor.getValue().getAuthorities().isEmpty());
    verifyChainContinued();
  }

  private void verifyChainContinued() throws IOException, ServletException {
    verify(chain, times(1)).doFilter(request, response);
  }

  private void verifyChainNotContinued() throws IOException, ServletException {
    verify(chain, never()).doFilter(request, response);
  }


}