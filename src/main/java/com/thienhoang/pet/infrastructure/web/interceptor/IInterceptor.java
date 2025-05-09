package com.thienhoang.pet.infrastructure.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface IInterceptor {

  boolean onIntercept(HttpServletRequest request, HttpServletResponse response, Object handler);
}
