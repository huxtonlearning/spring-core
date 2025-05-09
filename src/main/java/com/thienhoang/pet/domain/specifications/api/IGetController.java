package com.thienhoang.pet.domain.specifications.api;

import com.thienhoang.pet.domain.specifications.models.values.HeaderContext;
import com.thienhoang.pet.domain.specifications.services.IGetService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface IGetController<E, ID, RES> {

  default IGetService<E, ID, RES> getGetService() {

    return null;
  }

  /**
   * Lấy thông tin một entity theo ID.
   *
   * @param context HeaderContext từ request
   * @param id ID của entity cần lấy
   * @return RES Thông tin entity
   */
  @GetMapping(path = "/{id}")
  default RES getById(
      @Parameter(hidden = true) HeaderContext context,
      @Parameter(description = "ID of the user to retrieve", required = true) @PathVariable ID id) {
    if (getGetService() == null) {
      return null;
    }
    return getGetService().getById(context, id);
  }
}
