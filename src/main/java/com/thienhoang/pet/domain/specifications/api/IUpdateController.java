package com.thienhoang.pet.domain.specifications.api;

import com.thienhoang.pet.domain.specifications.models.values.HeaderContext;
import com.thienhoang.pet.domain.specifications.services.IUpdateService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface IUpdateController<E, ID, RES, REQ> {

  default IUpdateService<E, ID, RES, REQ> getUpdateService() {

    return null;
  }

  /**
   * Cập nhật một entity theo ID.
   *
   * @param context HeaderContext từ request
   * @param id ID của entity cần cập nhật
   * @param request Dữ liệu cập nhật
   * @return RES Đối tượng đã cập nhật
   */
  @PutMapping(path = "/{id}")
  default ResponseEntity<RES> update(
      @Parameter(hidden = true) HeaderContext context,
      @PathVariable ID id,
      @RequestBody REQ request) {
    if (getUpdateService() == null) {
      return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }
    return ResponseEntity.ok(getUpdateService().update(context, id, request));
  }
}
