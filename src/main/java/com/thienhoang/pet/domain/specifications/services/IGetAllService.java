package com.thienhoang.pet.domain.specifications.services;

import com.thienhoang.pet.domain.specifications.models.values.HeaderContext;
import com.thienhoang.pet.domain.utils.FnCommon;
import com.thienhoang.pet.domain.utils.GenericTypeUtils;
import java.util.function.BiFunction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IGetAllService<E, RES, P> {

  default JpaSpecificationExecutor<E> getSpecificationExecutor() {

    return null;
  }

  default Specification<E> buildQuery(HeaderContext context, P params) {
    return (root, query, cb) -> cb.conjunction();
  }

  default Page<RES> getAll(
      HeaderContext context,
      Pageable pageable,
      P params,
      BiFunction<HeaderContext, E, RES> mappingResponseHandler) {
    Page<E> data = getSpecificationExecutor().findAll(buildQuery(context, params), pageable);

    return data.map(item -> mappingResponseHandler.apply(context, item));
  }

  default Page<RES> getAll(HeaderContext context, Pageable pageable, P params) {

    return getAll(context, pageable, params, this::mappingPageResponse);
  }

  default RES mappingPageResponse(HeaderContext context, E item) {
    RES resItem = GenericTypeUtils.getNewInstance(this);

    FnCommon.copyProperties(resItem, item);

    return resItem;
  }
}
