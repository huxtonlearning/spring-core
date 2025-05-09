package com.thienhoang.pet.infrastructure.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class Interceptor implements HandlerInterceptor {

  private final List<IInterceptor> interceptors;

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {

    return interceptors.stream()
        .allMatch(interceptor -> interceptor.onIntercept(request, response, handler));
  }
}
