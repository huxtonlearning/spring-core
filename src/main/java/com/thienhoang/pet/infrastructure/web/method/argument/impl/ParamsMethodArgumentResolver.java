package com.thienhoang.pet.infrastructure.web.method.argument.impl;

import com.thienhoang.pet.domain.specifications.models.values.params.IParams;
import com.thienhoang.pet.domain.utils.JsonParserUtils;
import com.thienhoang.pet.domain.utils.ParamsKeys;
import com.thienhoang.pet.infrastructure.web.method.argument.IMethodArgument;
import java.lang.reflect.InvocationTargetException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class ParamsMethodArgumentResolver implements IMethodArgument {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return IParams.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory)
      throws NoSuchMethodException,
          InvocationTargetException,
          InstantiationException,
          IllegalAccessException {

    String filter = webRequest.getParameter(ParamsKeys.FILTER);

    Class<?> parameterType = parameter.getParameterType();
    Object params = parameterType.getDeclaredConstructor().newInstance();
    if (!StringUtils.hasText(filter)) {
      return params;
    }

    try {
      return JsonParserUtils.entity(filter, parameterType);

    } catch (Exception e) {

      return params;
    }
  }
}
