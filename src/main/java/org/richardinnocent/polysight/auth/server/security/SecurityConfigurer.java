package org.richardinnocent.polysight.auth.server.security;

import org.richardinnocent.polysight.auth.server.Qualifiers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@SuppressWarnings("unused")
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

  private final PublicPrivateKeyProvider publicPrivateKeyProvider;
  private final AuthenticationProvider authenticationProvider;

  public SecurityConfigurer(
      @Qualifier(Qualifiers.JWT) PublicPrivateKeyProvider publicPrivateKeyProvider,
      AuthenticationProvider authenticationProvider
  ) {
    this.publicPrivateKeyProvider = publicPrivateKeyProvider;
    this.authenticationProvider = authenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .httpBasic().disable()
        .sessionManagement().disable()
        .addFilter(
            new JWTAuthorizationFilter(
                authenticationManager(),
                publicPrivateKeyProvider,
                new SecurityContextAuthenticationFacade()
            )
        )
        .authorizeRequests().antMatchers("/authenticate").permitAll()
        .and()
        .authorizeRequests().anyRequest().authenticated();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(authenticationProvider);
  }

}
