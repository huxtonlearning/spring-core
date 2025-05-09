package com.thienhoang.pet.domain.specifications.api;

import com.thienhoang.pet.domain.specifications.services.*;

/**
 * Interface định nghĩa chuẩn RESTful cho các controller CRUD (Create, Read, Update, Delete). Có thể
 * áp dụng lại cho nhiều entity khác nhau nhằm giảm lặp code.
 *
 * @param <RES> Kiểu dữ liệu trả về (Response DTO)
 * @param <ID> Kiểu ID của entity (VD: Long, UUID)
 * @param <REQ> Kiểu dữ liệu request đầu vào (Request DTO)
 */
public interface ICrudController<E, ID, RES, REQ>
    extends ICreateController<E, ID, RES, REQ>,
        IUpdateController<E, ID, RES, REQ>,
        IDeleteController<E, ID>,
        IGetController<E, ID, RES> {

  default ICrudService<E, ID, RES, REQ> getCrudService() {

    return null;
  }

  @Override
  default ICreateService<E, ID, RES, REQ> getCreateService() {
    return getCrudService();
  }

  @Override
  default IUpdateService<E, ID, RES, REQ> getUpdateService() {
    return getCrudService();
  }

  @Override
  default IDeleteService<E, ID> getDeleteService() {
    return getCrudService();
  }

  @Override
  default IGetService<E, ID, RES> getGetService() {
    return getCrudService();
  }
}
