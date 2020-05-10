package org.richardinnocent.security;

import org.richardinnocent.Qualifiers;
import org.richardinnocent.services.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@SuppressWarnings("unused")
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

  private final UserService userService;
  private final PublicPrivateKeyProvider publicPrivateKeyProvider;
  private final AuthenticationProvider authenticationProvider;

  public SecurityConfigurer(
      UserService userService,
      @Qualifier(Qualifiers.JWT) PublicPrivateKeyProvider publicPrivateKeyProvider,
      AuthenticationProvider authenticationProvider) {
    this.userService = userService;
    this.publicPrivateKeyProvider = publicPrivateKeyProvider;
    this.authenticationProvider = authenticationProvider;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.sessionManagement().disable()
        .addFilter(
            new JWTAuthenticationFilter(
                authenticationManager(), publicPrivateKeyProvider, userService))
        .addFilter(
            new JWTAuthorizationFilter(
                authenticationManager(),
                publicPrivateKeyProvider,
                new SecurityContextAuthenticationFacade()))
        .formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/profile").and()
        .logout().deleteCookies(JWTCookieFields.COOKIE_NAME).logoutSuccessUrl("/login").and()
        .authorizeRequests()
        .antMatchers("/signup", "/favicon.ico", "/error", "/api/v1/security/publickey").permitAll()
        .anyRequest().authenticated();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(authenticationProvider);
  }

}
