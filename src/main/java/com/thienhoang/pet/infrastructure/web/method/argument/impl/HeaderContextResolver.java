package com.thienhoang.pet.infrastructure.web.method.argument.impl;

import com.thienhoang.pet.domain.specifications.models.values.HeaderContext;
import com.thienhoang.pet.infrastructure.web.method.argument.IMethodArgument;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class HeaderContextResolver implements IMethodArgument {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(HeaderContext.class);
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {

    HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

    // Ví dụ: lấy header Authorization và Accept-Language
    String authToken = request.getHeader("Authorization");
    String locale = request.getHeader("Accept-Language");

    // Bạn có thể parse JWT, hoặc lấy userId, tenantId... ở đây nếu cần

    return HeaderContext.builder().userId(1L).name("Hoang").build();
  }
}
