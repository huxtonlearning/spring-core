package com.thienhoang.pet.infrastructure.web;

import com.thienhoang.pet.infrastructure.web.interceptor.Interceptor;
import com.thienhoang.pet.infrastructure.web.method.argument.MethodArgument;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final Interceptor interceptor;
  private final MethodArgument methodArgument;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    resolvers.addAll(methodArgument.getMethodArgumentList());
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {}

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(interceptor);
  }
}
