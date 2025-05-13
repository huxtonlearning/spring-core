package com.thienhoang.pet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @Import(com.thienhoang.common.config.Config.class)
public class PetApplication {

  public static void main(String[] args) {
    SpringApplication.run(PetApplication.class, args);
  }
}
