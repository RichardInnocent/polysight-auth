package org.richardinnocent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "org.richardinnocent.controller")
@SpringBootApplication
public class PolysightAuthApp {

  public static void main(String[] args) {
    SpringApplication.run(PolysightAuthApp.class, args);
  }

}
