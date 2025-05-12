package com.thienhoang.pet.domain.specifications.services;

import com.thienhoang.pet.domain.specifications.models.values.HeaderContext;
import com.thienhoang.pet.domain.utils.FnCommon;
import com.thienhoang.pet.domain.utils.GenericTypeUtils;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import org.apache.logging.log4j.util.TriConsumer;

public interface ICreateService<E, ID, RES, REQ>
    extends IJpaRepositoryProvider<E, ID>, IResponseMapper<E, RES> {

  /**
   * Tạo entity mới từ request và lưu vào database. Có hỗ trợ validation và mapping tùy chỉnh.
   *
   * @param context Header context (chứa thông tin người dùng, locale, ...)
   * @param request Dữ liệu từ client gửi lên
   * @param validationCreateHandler Hàm callback kiểm tra dữ liệu đầu vào (có thể throw lỗi)
   * @param mappingEntityHandler Hàm callback để gán dữ liệu tùy chỉnh vào entity
   * @return Entity sau khi được lưu vào database
   */
  default RES create(
      HeaderContext context,
      REQ request,
      TriConsumer<HeaderContext, E, REQ> validationCreateHandler,
      TriConsumer<HeaderContext, E, REQ> mappingEntityHandler,
      BiConsumer<HeaderContext, E> mappingAuditingHandler,
      TriConsumer<HeaderContext, E, REQ> postHandler,
      BiFunction<HeaderContext, E, RES> mappingResponseHandler) {
    E entity = GenericTypeUtils.getNewInstance(this); // Tạo entity mới bằng Reflection

    if (validationCreateHandler != null) {
      validationCreateHandler.accept(context, entity, request);
    }

    if (mappingEntityHandler != null) {
      mappingEntityHandler.accept(context, entity, request); // Gọi mapping tùy chỉnh
    }

    if (mappingAuditingHandler != null) {
      mappingAuditingHandler.accept(context, entity); // Gọi mapping tùy chỉnh
    }
    entity = getJpaRepository().save(entity);

    postHandler.accept(context, entity, request);

    if (mappingResponseHandler == null) {
      throw new IllegalArgumentException("mappingResponseHandler must not be null");
    }
    return mappingResponseHandler.apply(context, entity); // Lưu entity vào DB
  }

  /** Hàm tạo mặc định nếu không truyền vào hàm validate/mapping riêng. */
  default RES create(HeaderContext context, REQ request) {
    return create(
        context,
        request,
        this::validateCreateRequest,
        this::mappingCreateEntity,
        this::mappingCreateAuditingEntity,
        this::postCreateHandler,
        this::mapResponse);
  }

  /** Hàm validate mặc định (không làm gì) — override trong implementation nếu cần. */
  default void validateCreateRequest(HeaderContext context, E entity, REQ request) {}

  default void mappingCreateAuditingEntity(HeaderContext context, E entity) {
    if (context != null) {
      GenericTypeUtils.updateData(entity, "creatorId", context.getUserId());
      GenericTypeUtils.updateData(entity, "modifierId", context.getUserId());
    }
  }

  /** Hàm mapping mặc định (không làm gì) — override nếu cần xử lý riêng. */
  default void mappingCreateEntity(HeaderContext context, E entity, REQ request) {
    FnCommon.copyProperties(entity, request); // Copy các field giống nhau từ request sang entity
  }

  default void postCreateHandler(HeaderContext context, E entity, REQ request) {}
}
