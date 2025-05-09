package com.thienhoang.pet.domain.specifications.services;

import com.thienhoang.pet.domain.specifications.models.values.HeaderContext;
import org.apache.logging.log4j.util.TriConsumer;

public interface IDeleteService<E, ID> extends IGetEntityService<E, ID> {

  /** Xóa entity theo ID, có thể validate trước khi xóa. */
  default void delete(
      HeaderContext context, ID id, TriConsumer<HeaderContext, ID, E> validationHandler) {
    E entity = getEntityById(context, id); // Lấy entity từ DB

    if (validationHandler != null) {
      validationHandler.accept(context, id, entity); // Kiểm tra hợp lệ trước khi xóa
    }
    getJpaRepository().delete(entity); // Xóa khỏi DB
  }

  /** Hàm xóa mặc định không cần validate riêng. */
  default void delete(HeaderContext context, ID id) {
    delete(context, id, this::validateDelete);
  }

  // Hàm validate mặc định khi xóa
  default void validateDelete(HeaderContext context, ID id, E entity) {}
}
