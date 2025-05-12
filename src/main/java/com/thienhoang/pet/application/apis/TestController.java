package com.thienhoang.pet.application.apis;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "tests")
public class TestController {

  @GetMapping
  public ErrorResponse test() {

    return ErrorResponse.builder(
            new RuntimeException("12412"), HttpStatus.INTERNAL_SERVER_ERROR, "asfasfas")
        .build();
  }
}
