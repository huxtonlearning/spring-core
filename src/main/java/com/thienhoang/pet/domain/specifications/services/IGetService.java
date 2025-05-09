package com.thienhoang.pet.domain.specifications.services;

import com.thienhoang.pet.domain.specifications.models.values.HeaderContext;

public interface IGetService<E, ID, RES>
    extends IJpaRepositoryProvider<E, ID>, IResponseMapper<E, RES>, IGetEntityService<E, ID> {

  /** Tìm entity theo ID, ném lỗi 404 nếu không tìm thấy. */
  default RES getById(HeaderContext context, ID id) {

    E entity = getEntityById(context, id);

    return mapResponse(context, entity);
  }
}
