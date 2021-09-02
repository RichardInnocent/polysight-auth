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
  public void doFilterInternal_NoAuthorizationHeader_AuthorizationNotSet() throws IOException, ServletException {
    when(request.getHeader(JwtFields.HEADER_NAME)).thenReturn(null);

    filter.doFilterInternal(request, response, chain);

    verify(authenticationFacade, never()).setAuthentication(any());
    verifyChainContinued();
  }

  @Test
  public void doFilterInternal_EmptyJwtCookie_AuthorizationNotSet() throws IOException, ServletException {
    when(request.getHeader(JwtFields.HEADER_NAME)).thenReturn("");

    filter.doFilterInternal(request, response, chain);

    verify(authenticationFacade, never()).setAuthentication(any());
    verifyChainContinued();
  }

  @Test
  public void doFilterInternal_InvalidJwtCookie_AuthorizationNotSet()
      throws IOException, ServletException {
    when(request.getHeader(JwtFields.HEADER_NAME)).thenReturn("not a valid token");

    filter.doFilterInternal(request, response, chain);

    verify(authenticationFacade, never()).setAuthentication(any());
    verifyChainContinued();
  }

  @Test
  public void doFilterInternal_ValidJwtCookie_AuthorizationSet()
      throws IOException, ServletException {
    long id = 10L;
    String email = "test@polysight.com";
    String authority1 = "authority1";
    String authority2 = "authority2";
    String authority3 = "authority3";
    String authorities = "[\"" + authority1 + "\",\"" + authority2 + "\",\"" + authority3 + "\"]";

    String token = JWT
        .create()
        .withIssuer(JwtFields.ISSUER)
        .withClaim(JwtFields.USER_ID_CLAIM_KEY, id)
        .withClaim(JwtFields.EMAIL_CLAIM_KEY, email)
        .withClaim(JwtFields.AUTHORITIES_CLAIM_KEY, authorities)
        .withExpiresAt(new Date(System.currentTimeMillis() + 10_000L))
        .sign(
            Algorithm.ECDSA512(
                (ECPublicKey) keyProvider.getPublicKey(),
                (ECPrivateKey) keyProvider.getPrivateKey()
            )
        );

    when(request.getHeader(JwtFields.HEADER_NAME)).thenReturn("Bearer " + token);

    ArgumentCaptor<Authentication> authenticationCaptor =
        ArgumentCaptor.forClass(Authentication.class);

    filter.doFilterInternal(request, response, chain);

    verify(authenticationFacade, times(1))
        .setAuthentication(authenticationCaptor.capture());

    SimpleAuthenticatedUser expectedUser = SimpleAuthenticatedUser.of(id, email);
    assertEquals(expectedUser, authenticationCaptor.getValue().getPrincipal());
    Collection<? extends GrantedAuthority> detectedAuthorities =
        authenticationCaptor.getValue().getAuthorities();

    assertTrue(
        detectedAuthorities.stream().allMatch(auth -> authorities.contains(auth.getAuthority()))
    );

    assertEquals(3, detectedAuthorities.size());
    verifyChainContinued();
  }

  private void verifyChainContinued() throws IOException, ServletException {
    verify(chain, times(1)).doFilter(request, response);
  }

}