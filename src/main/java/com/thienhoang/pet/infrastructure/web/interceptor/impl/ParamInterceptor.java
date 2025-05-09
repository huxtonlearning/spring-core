package com.thienhoang.pet.infrastructure.web.interceptor.impl;

import com.thienhoang.pet.infrastructure.web.interceptor.IInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class ParamInterceptor implements IInterceptor {

  @Override
  public boolean onIntercept(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    request.getMethod();

    return true;
  }
}
