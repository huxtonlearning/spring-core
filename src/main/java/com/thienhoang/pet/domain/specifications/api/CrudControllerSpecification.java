package com.thienhoang.pet.domain.specifications.api;

import com.thienhoang.pet.domain.specifications.models.values.HeaderContext;
import com.thienhoang.pet.domain.specifications.services.CrudServiceSpecification;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

/**
 * Interface định nghĩa chuẩn RESTful cho các controller CRUD (Create, Read, Update, Delete). Có thể
 * áp dụng lại cho nhiều entity khác nhau nhằm giảm lặp code.
 *
 * @param <RES> Kiểu dữ liệu trả về (Response DTO)
 * @param <ID> Kiểu ID của entity (VD: Long, UUID)
 * @param <REQ> Kiểu dữ liệu request đầu vào (Request DTO)
 */
public interface CrudControllerSpecification<E, ID, RES, REQ> {

  CrudServiceSpecification<E, ID, RES, REQ> getService();

  /**
   * Tạo mới một entity.
   *
   * @param context HeaderContext tự động resolve từ request (thường chứa: token, locale, userId...)
   * @param request Dữ liệu gửi lên từ client để tạo entity
   * @return RES Đối tượng trả về sau khi tạo (thường là bản ghi vừa tạo)
   */
  @PostMapping
  default RES create(@Parameter(hidden = true) HeaderContext context, @RequestBody REQ request) {

    return getService().create(context, request);
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
  default RES update(
      @Parameter(hidden = true) HeaderContext context,
      @PathVariable ID id,
      @RequestBody REQ request) {
    return getService().update(context, id, request);
  }

  /**
   * Xoá một entity theo ID.
   *
   * @param context HeaderContext từ request
   * @param id ID của entity cần xoá
   */
  @DeleteMapping(path = "/{id}")
  default void delete(@Parameter(hidden = true) HeaderContext context, @PathVariable ID id) {
    getService().delete(context, id);
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
    return getService().getById(context, id);
  }
}
