package com.thienhoang.pet.infrastructure.web.method.argument.impl;

import com.thienhoang.pet.domain.utils.JsonParserUtils;
import com.thienhoang.pet.domain.utils.NumberUtils;
import com.thienhoang.pet.domain.utils.ParamsKeys;
import com.thienhoang.pet.infrastructure.web.method.argument.IMethodArgument;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class PageableMethodArgumentResolver implements IMethodArgument {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return Pageable.class.isAssignableFrom(parameter.getParameterType());
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {

    // Handle sort
    String sortStr = webRequest.getParameter(ParamsKeys.SORT);
    Sort sort = Sort.unsorted();
    Map<String, String> sortMap = JsonParserUtils.toMap(sortStr);
    if (!sortMap.isEmpty()) {
      sort =
          Sort.by(
              sortMap.entrySet().stream()
                  .map(
                      entry -> {
                        Sort.Direction direction =
                            Sort.Direction.fromString(
                                SortEnum.fromOriginal(NumberUtils.parseInt(entry.getValue(), 1))
                                    .name());
                        return new Sort.Order(direction, entry.getKey());
                      })
                  .collect(Collectors.toList()));
    }
    // Handle page
    String pageStr = webRequest.getParameter(ParamsKeys.PAGE);
    String sizeStr = webRequest.getParameter(ParamsKeys.SIZE);

    int page = NumberUtils.parseInt(pageStr, 0);
    int size = NumberUtils.parseInt(sizeStr, 20);
    //    size = Math.min(size, properties.getMaxPageSize());

    if (page == 0) {
      return Pageable.unpaged(sort);
    } else {
      return PageRequest.of(page - 1, size, sort);
    }
  }

  @Getter
  enum SortEnum {
    desc(-1),
    asc(1);
    private final int original;

    SortEnum(int original) {
      this.original = original;
    }

    static SortEnum fromOriginal(int original) {

      for (SortEnum sortEnum : SortEnum.values()) {
        if (sortEnum.original == original) {
          return sortEnum;
        }
      }

      throw new IllegalArgumentException("Invalid sort value: " + original);
    }
  }
}
