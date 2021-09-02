package org.richardinnocent.polysight.auth.server.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.IOException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.richardinnocent.polysight.auth.server.http.controller.MissingParametersException;
import org.richardinnocent.polysight.auth.server.models.user.AccountStatus;
import org.richardinnocent.polysight.auth.server.models.user.PolysightUser;
import org.richardinnocent.polysight.auth.server.services.user.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class JWTAuthenticationFilterTest {

  private static final String EMAIL_KEY = "email";
  private static final String PASSWORD_KEY = "password";

  private static PublicPrivateKeyProvider keyProvider;

  private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
  private final UserService userService = mock(UserService.class);

  private final JWTAuthenticationFilter authenticationFilter =
      new JWTAuthenticationFilter(authenticationManager, keyProvider, userService);

  @BeforeAll
  public static void setUpKeyProvider() throws NoSuchAlgorithmException {
    keyProvider = new JWTPublicPrivateKeyProvider(KeyPairGenerator.getInstance("EC"));
  }

  @Test
  public void testAttemptAuthenticationWithValidCredentials() {
    String email = "valid.user@polysight.com";
    String rawPassword = "rawPassword";
    String encryptedPassword = "encryptedPassword";
    String salt = "salt";

    PolysightUser user = mock(PolysightUser.class);
    when(user.getEmail()).thenReturn(email);
    when(user.getPassword()).thenReturn(encryptedPassword);
    when(user.getPasswordSalt()).thenReturn(salt);
    when(user.getAccountStatus()).thenReturn(AccountStatus.ACTIVE);

    when(userService.findByEmail(eq(email))).thenReturn(Optional.of(user));

    Authentication returnedAuthentication = mock(Authentication.class);

    ArgumentCaptor<Authentication> authenticationArgumentCaptor =
        ArgumentCaptor.forClass(Authentication.class);
    when(authenticationManager.authenticate(any())).thenReturn(returnedAuthentication);

    assertEquals(returnedAuthentication,
                 authenticationFilter.attemptAuthentication(
                     createRequestWithCredentials(email, rawPassword), null));

    verify(authenticationManager).authenticate(authenticationArgumentCaptor.capture());

    Authentication authentication = authenticationArgumentCaptor.getValue();

    assertTrue(authentication instanceof UsernamePasswordAuthenticationToken);
    UsernamePasswordAuthenticationToken authToken =
        (UsernamePasswordAuthenticationToken) authentication;

    assertEquals(email, authToken.getPrincipal());
    assertEquals(rawPassword + salt, authToken.getCredentials());
    assertEquals(Collections.emptyList(), authToken.getAuthorities());
  }

  @Test
  public void testAttemptAuthenticationWithNoEmailProvided() {
    HttpServletRequest request = createRequestWithCredentials(null, "password");
    try {
      authenticationFilter.attemptAuthentication(request, null);
      fail("No exception thrown");
    } catch (MissingParametersException e) {
      assertEquals(Collections.singletonList(EMAIL_KEY), e.getMissingParameters());
    }
  }

  @Test
  public void testAttemptAuthenticationWithNoPasswordProvided() {
    HttpServletRequest request = createRequestWithCredentials("email", null);
    try {
      authenticationFilter.attemptAuthentication(request, null);
      fail("No exception thrown");
    } catch (MissingParametersException e) {
      assertEquals(Collections.singletonList(PASSWORD_KEY), e.getMissingParameters());
    }
  }

  @Test
  public void testAttemptValidationWithUnfoundUserThrowsBadCredentialsException() {
    String email = "nonexistent@polysight.com";
    HttpServletRequest request = createRequestWithCredentials(email, "password");
    when(userService.findByEmail(eq(email))).thenReturn(Optional.empty());
    assertThrows(
        BadCredentialsException.class,
        () -> authenticationFilter.attemptAuthentication(request, null)
    );
  }

  @Test
  public void testAttemptValidationWithDisabledUserThrowsDisabledException() {
    String email = "nonexistent@polysight.com";
    HttpServletRequest request = createRequestWithCredentials(email, "password");
    PolysightUser user = mock(PolysightUser.class);
    when(user.getAccountStatus()).thenReturn(AccountStatus.DISABLED);
    when(userService.findByEmail(eq(email))).thenReturn(Optional.of(user));
    assertThrows(
        DisabledException.class,
        () -> authenticationFilter.attemptAuthentication(request, null)
    );
  }

  private HttpServletRequest createRequestWithCredentials(String email, String password) {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getParameter(EMAIL_KEY)).thenReturn(email);
    when(request.getParameter(PASSWORD_KEY)).thenReturn(password);
    return request;
  }

  @Test
  public void testJWTIsCreatedOnSuccess() throws IOException, ServletException {
    String email = "valid.user@polysight.com";
    Authentication authentication = mock(Authentication.class);
    User principal = mock(User.class);
    Collection<GrantedAuthority> authorities = Arrays.asList(()->"authority1", ()->"authority2");
    when(principal.getUsername()).thenReturn(email);
    when(principal.getAuthorities()).thenReturn(authorities);
    when(authentication.getPrincipal()).thenReturn(principal);

    HttpServletResponse response = mock(HttpServletResponse.class);
    ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);

    authenticationFilter.successfulAuthentication(
        mock(HttpServletRequest.class), response, mock(FilterChain.class), authentication);
    verify(response).addCookie(cookieCaptor.capture());

    Cookie cookie = cookieCaptor.getValue();
    assertNotNull(cookie);
    assertTrue(cookie.isHttpOnly());
    assertEquals(864_000, cookie.getMaxAge());
    assertEquals(JwtFields.HEADER_NAME, cookie.getName());

    DecodedJWT jwt = JWT.require(Algorithm.ECDSA512((ECPublicKey) keyProvider.getPublicKey(),
                                                    (ECPrivateKey) keyProvider.getPrivateKey()))
                        .build()
                        .verify(cookie.getValue());

    assertEquals(email, jwt.getClaim(JwtFields.EMAIL_CLAIM_KEY).asString());
    assertEquals("[\"authority1\",\"authority2\"]",
                 jwt.getClaim(JwtFields.AUTHORITIES_CLAIM_KEY).asString());
    Date expirationDate = jwt.getExpiresAt();
    assertTrue(expirationDate.after(new Date()));
  }

  @Test
  public void testJWTIsCreatedOnSuccessWithNullAuthoritiesHasEmptyAuthorityClaim()
      throws IOException, ServletException {
    String email = "valid.user@polysight.com";
    Authentication authentication = mock(Authentication.class);
    User principal = mock(User.class);
    when(principal.getUsername()).thenReturn(email);
    when(principal.getAuthorities()).thenReturn(null);
    when(authentication.getPrincipal()).thenReturn(principal);

    HttpServletResponse response = mock(HttpServletResponse.class);
    ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);

    authenticationFilter.successfulAuthentication(
        mock(HttpServletRequest.class), response, mock(FilterChain.class), authentication);
    verify(response).addCookie(cookieCaptor.capture());

    Cookie cookie = cookieCaptor.getValue();
    assertNotNull(cookie);
    assertTrue(cookie.isHttpOnly());
    assertEquals(864_000, cookie.getMaxAge());
    assertEquals(JwtFields.HEADER_NAME, cookie.getName());

    DecodedJWT jwt = JWT.require(Algorithm.ECDSA512((ECPublicKey) keyProvider.getPublicKey(),
                                                    (ECPrivateKey) keyProvider.getPrivateKey()))
                        .build()
                        .verify(cookie.getValue());

    assertEquals(email, jwt.getClaim(JwtFields.EMAIL_CLAIM_KEY).asString());
    assertEquals("[]",
                 jwt.getClaim(JwtFields.AUTHORITIES_CLAIM_KEY).asString());
    Date expirationDate = jwt.getExpiresAt();
    assertTrue(expirationDate.after(new Date()));
  }

}