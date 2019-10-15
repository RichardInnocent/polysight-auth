package org.richardinnocent;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;

@Configuration
@ComponentScan(basePackages = "org.richardinnocent")
@SuppressWarnings("unused")
public class PolysightAuthConfig {

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public StringKeyGenerator saltGenerator() {
    return KeyGenerators.string();
  }

}
