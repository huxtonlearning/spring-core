package com.thienhoang.pet.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule()); // Hỗ trợ Java 8 time
    //    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Bật để dùng ISO
    //    mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Bật để dùng timestamp
    // (unix)
    return mapper;
  }
}
