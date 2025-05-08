// package com.thienhoang.pet.infrastructure.open.api;
//
// import io.swagger.v3.oas.models.Components;
// import io.swagger.v3.oas.models.OpenAPI;
// import io.swagger.v3.oas.models.media.Schema;
// import org.springdoc.core.customizers.OpenApiCustomizer;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// @Configuration
// public class OpenApiConfig {
//
//  @Bean
//  public OpenAPI customOpenAPI() {
//    return new OpenAPI().components(new Components());
//  }
//
//  @Bean
//  public OpenApiCustomizer customiseDateSchemas() {
//    return openApi -> {
//      // Tạo schema cho Timestamp và Instant tự động nhận diện là long (epoch milliseconds)
//      Schema<?> timestampSchema = new Schema<>().type("integer").format("int64");
//
//      openApi.getComponents().addSchemas("Timestamp", timestampSchema);
//      openApi.getComponents().addSchemas("Date", timestampSchema);
//      openApi.getComponents().addSchemas("Instant", timestampSchema);
//    };
//  }
// }
