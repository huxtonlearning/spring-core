package com.thienhoang.pet.domain.specifications.services;

import com.thienhoang.pet.domain.specifications.models.values.HeaderContext;
import com.thienhoang.pet.domain.utils.FnCommon;
import com.thienhoang.pet.domain.utils.GenericTypeUtils;

public interface IResponseMapper<E, RES> {

  default RES mapResponse(HeaderContext context, E entity) {

    RES res = GenericTypeUtils.getNewInstance(this);

    FnCommon.copyProperties(res, entity);

    return res;
  }
}
