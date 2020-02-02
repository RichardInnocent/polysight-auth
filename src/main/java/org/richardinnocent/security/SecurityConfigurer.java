package org.richardinnocent.security;

import org.richardinnocent.persistence.user.PolysightUserDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

  private final PolysightUserDAO userDAO;
  private final PublicPrivateKeyProvider publicPrivateKeyProvider;
  private final AuthenticationProvider authenticationProvider;

  public SecurityConfigurer(PolysightUserDAO userDAO,
                            @Qualifier("jwt") PublicPrivateKeyProvider publicPrivateKeyProvider,
                            AuthenticationProvider authenticationProvider) {
    this.userDAO = userDAO;
    this.publicPrivateKeyProvider = publicPrivateKeyProvider;
    this.authenticationProvider = authenticationProvider;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.sessionManagement().disable();

    http.authorizeRequests()
        .antMatchers("/login", "/signup")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .addFilter(
            new JWTAuthenticationFilter(
                authenticationManager(), publicPrivateKeyProvider, userDAO))
        .addFilter(new JWTAuthorizationFilter(authenticationManager(), publicPrivateKeyProvider))
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .formLogin()
        .loginPage("/login")
        .successForwardUrl("/profile");
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(authenticationProvider);
  }

}
