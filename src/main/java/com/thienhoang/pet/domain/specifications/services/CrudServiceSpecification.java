package com.thienhoang.pet.domain.specifications.services;

import com.thienhoang.pet.domain.specifications.models.values.HeaderContext;
import com.thienhoang.pet.domain.utils.FnCommon;
import com.thienhoang.pet.domain.utils.function.interfaces.QuadConsumer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import org.apache.logging.log4j.util.TriConsumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Giao diện chuẩn hóa cho thao tác CRUD, có thể tái sử dụng cho nhiều entity khác nhau.
 *
 * @param <E> Entity class (VD: User, Product)
 * @param <ID> Kiểu ID (VD: Long, UUID)
 * @param <REQ> Request DTO từ client (VD: UserCreateRequest)
 */
public interface CrudServiceSpecification<E, ID, RES, REQ> {

  /** Phương thức cần được override để cung cấp repository cho entity. */
  JpaRepository<E, ID> getRepository();

  /**
   * Tạo entity mới từ request và lưu vào database. Có hỗ trợ validation và mapping tùy chỉnh.
   *
   * @param context Header context (chứa thông tin người dùng, locale, ...)
   * @param request Dữ liệu từ client gửi lên
   * @param validationHandler Hàm callback kiểm tra dữ liệu đầu vào (có thể throw lỗi)
   * @param mappingEntityHandler Hàm callback để gán dữ liệu tùy chỉnh vào entity
   * @return Entity sau khi được lưu vào database
   */
  default RES create(
      HeaderContext context,
      REQ request,
      TriConsumer<HeaderContext, REQ, E> validationHandler,
      TriConsumer<HeaderContext, REQ, E> mappingEntityHandler,
      BiFunction<HeaderContext, E, RES> mappingResponseHandler) {
    E entity = getNewEntityInstance(); // Tạo entity mới bằng Reflection

    validationHandler.accept(context, request, entity); // Gọi hàm kiểm tra hợp lệ dữ liệu

    FnCommon.copyProperties(entity, request); // Copy các field giống nhau từ request sang entity

    mappingEntityHandler.accept(context, request, entity); // Gọi mapping tùy chỉnh

    return mappingResponseHandler.apply(context, getRepository().save(entity)); // Lưu entity vào DB
  }

  /** Hàm tạo mặc định nếu không truyền vào hàm validate/mapping riêng. */
  default RES create(HeaderContext context, REQ request) {
    return create(
        context,
        request,
        this::validateCreateRequest,
        this::mappingCreateEntity,
        this::mappingResponse);
  }

  /** Hàm validate mặc định (không làm gì) — override trong implementation nếu cần. */
  default void validateCreateRequest(HeaderContext context, REQ request, E entity) {}

  /** Hàm mapping mặc định (không làm gì) — override nếu cần xử lý riêng. */
  default void mappingCreateEntity(HeaderContext context, REQ request, E entity) {}

  /** Hàm mapping mặc định (không làm gì) — override nếu cần xử lý riêng. */
  RES mappingResponse(HeaderContext context, E entity);

  /**
   * Cập nhật entity hiện có theo ID, có validate và mapping tuỳ chỉnh.
   *
   * @param context Header context
   * @param id ID của entity
   * @param request Request chứa thông tin cần cập nhật
   * @param validationHandler Hàm kiểm tra hợp lệ trước khi update
   * @param mappingHandler Hàm mapping dữ liệu request vào entity
   * @return Entity đã cập nhật
   */
  default RES update(
      HeaderContext context,
      ID id,
      REQ request,
      QuadConsumer<HeaderContext, ID, REQ, E> validationHandler,
      TriConsumer<HeaderContext, REQ, E> mappingHandler,
      BiFunction<HeaderContext, E, RES> mappingResponseHandler) {
    E entity = getEntityById(context, id); // Lấy entity từ DB, nếu không có thì ném lỗi 404

    validationHandler.accept(context, id, request, entity); // Kiểm tra hợp lệ

    FnCommon.copyProperties(entity, request); // Gán dữ liệu chung từ request

    mappingHandler.accept(context, request, entity); // Gọi hàm mapping tùy chỉnh

    return mappingResponseHandler.apply(context, getRepository().save(entity)); // Lưu lại vào DB
  }

  /** Cập nhật mặc định nếu không cần validate/mapping riêng. */
  default RES update(HeaderContext context, ID id, REQ request) {
    return update(
        context,
        id,
        request,
        this::validateUpdateRequest,
        this::mappingUpdateEntity,
        this::mappingResponse);
  }

  // Validate mặc định khi update
  default void validateUpdateRequest(HeaderContext context, ID id, REQ request, E entity) {}

  // Mapping mặc định khi update
  default void mappingUpdateEntity(HeaderContext context, REQ request, E entity) {}

  /** Xóa entity theo ID, có thể validate trước khi xóa. */
  default void delete(
      HeaderContext context, ID id, TriConsumer<HeaderContext, ID, E> validationHandler) {
    E entity = getEntityById(context, id); // Lấy entity từ DB

    validationHandler.accept(context, id, entity); // Kiểm tra hợp lệ trước khi xóa

    getRepository().delete(entity); // Xóa khỏi DB
  }

  /** Hàm xóa mặc định không cần validate riêng. */
  default void delete(HeaderContext context, ID id) {
    delete(context, id, this::validateDelete);
  }

  // Hàm validate mặc định khi xóa
  default void validateDelete(HeaderContext context, ID id, E entity) {}

  /** Tìm entity theo ID, ném lỗi 404 nếu không tìm thấy. */
  default E getEntityById(HeaderContext context, ID id) {
    return getRepository()
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity not found"));
  }

  /** Tìm entity theo ID, ném lỗi 404 nếu không tìm thấy. */
  default RES getById(HeaderContext context, ID id) {

    E entity = getEntityById(context, id);
    return mappingResponse(context, entity);
  }

  /**
   * Tạo instance mới của entity class thông qua reflection. Phương thức này sử dụng cơ chế
   * SuperTypeTokenHolder để giải quyết vấn đề Type Erasure.
   *
   * @return Instance mới của entity class
   * @throws RuntimeException nếu không thể tạo instance mới
   */
  @SuppressWarnings("unchecked")
  private E getNewEntityInstance() {
    try {
      // Lấy class thông qua helper cải tiến
      Class<E> entityClass = getEntityClassFromGeneric();

      if (entityClass == null) {
        throw new RuntimeException(
            "Không thể phát hiện entity class (E). Hãy đảm bảo rằng interface được triển khai với generic type cụ thể.");
      }

      // Log để debug
      System.out.println("Đã phát hiện entity class: " + entityClass.getName());

      // Tạo instance mới thông qua constructor không tham số
      return entityClass.getDeclaredConstructor().newInstance();
    } catch (InstantiationException
        | IllegalAccessException
        | NoSuchMethodException
        | InvocationTargetException e) {
      throw new RuntimeException(
          "Không thể tạo entity instance mới. Hãy đảm bảo entity class có constructor không tham số và có thể truy cập.",
          e);
    } catch (Exception e) {
      throw new RuntimeException(
          "Lỗi không xác định khi tạo entity instance mới: " + e.getMessage(), e);
    }
  }

  /**
   * Phương thức cải tiến để lấy entity class từ generic type. Sử dụng nhiều phương pháp bao gồm
   * type token và generic supertype scanning.
   *
   * @return Class của entity generic type E, hoặc null nếu không tìm thấy
   */
  @SuppressWarnings("unchecked")
  default Class<E> getEntityClassFromGeneric() {
    Class<?> implementationClass = getClass();

    // Cache lưu trữ generic type mappings để tối ưu hiệu suất
    Map<TypeVariable<?>, Type> typeVariableMap = new HashMap<>();

    // Lấy target interface với generic type E
    Class<?> targetInterface = findTargetInterface(implementationClass);

    if (targetInterface == null) {
      System.out.println("WARNING: Không tìm thấy target interface có generic type E");
      return null;
    }

    // Lấy TypeVariable đại diện cho E trong target interface
    TypeVariable<?>[] typeParams = targetInterface.getTypeParameters();
    if (typeParams.length == 0) {
      System.out.println("WARNING: Target interface không có type parameters");
      return null;
    }

    TypeVariable<?> entityTypeVar = typeParams[0]; // Giả sử E là type parameter đầu tiên

    // Tìm kiếm binding của TypeVariable trong implementation class
    populateTypeVariableMap(implementationClass, typeVariableMap);

    // Truy xuất actual type cho E
    Type actualType = typeVariableMap.get(entityTypeVar);

    if (actualType == null) {
      System.out.println("WARNING: Không thể ánh xạ TypeVariable đến actual type");
      // Thử phương pháp khác nếu phương pháp trên thất bại
      return findEntityClassFallback(implementationClass, targetInterface);
    }

    // Chuyển đổi actualType thành Class
    if (actualType instanceof Class) {
      return (Class<E>) actualType;
    } else if (actualType instanceof ParameterizedType) {
      Type rawType = ((ParameterizedType) actualType).getRawType();
      if (rawType instanceof Class) {
        return (Class<E>) rawType;
      }
    }

    // Một phương pháp fallback khác
    return findEntityClassFallback(implementationClass, targetInterface);
  }

  /**
   * Phương pháp dự phòng để tìm entity class. Quét trực tiếp thông qua các interface đã triển khai.
   */
  @SuppressWarnings("unchecked")
  default Class<E> findEntityClassFallback(Class<?> implementationClass, Class<?> targetInterface) {
    // Duyệt qua tất cả các interface đã triển khai
    for (Type genericInterface : implementationClass.getGenericInterfaces()) {
      if (genericInterface instanceof ParameterizedType) {
        ParameterizedType paramType = (ParameterizedType) genericInterface;

        if (paramType.getRawType().equals(targetInterface)) {
          Type[] typeArgs = paramType.getActualTypeArguments();
          if (typeArgs.length > 0 && typeArgs[0] instanceof Class) {
            System.out.println(
                "Đã tìm thấy entity class thông qua fallback: "
                    + ((Class<?>) typeArgs[0]).getName());
            return (Class<E>) typeArgs[0];
          }
        }
      }
    }

    // Nếu không tìm thấy trong interface trực tiếp, thử class cha
    Class<?> superClass = implementationClass.getSuperclass();
    if (superClass != null && superClass != Object.class) {
      // Kiểm tra class cha một cách đệ quy
      return findEntityClassInSuperclass(superClass, targetInterface);
    }

    return null;
  }

  /** Tìm kiếm entity class trong class cha. */
  @SuppressWarnings("unchecked")
  default Class<E> findEntityClassInSuperclass(Class<?> superClass, Class<?> targetInterface) {
    // Kiểm tra các interface của superclass
    for (Type genericInterface : superClass.getGenericInterfaces()) {
      if (genericInterface instanceof ParameterizedType) {
        ParameterizedType paramType = (ParameterizedType) genericInterface;

        if (paramType.getRawType().equals(targetInterface)) {
          Type[] typeArgs = paramType.getActualTypeArguments();
          if (typeArgs.length > 0 && typeArgs[0] instanceof Class) {
            System.out.println(
                "Đã tìm thấy entity class trong superclass: " + ((Class<?>) typeArgs[0]).getName());
            return (Class<E>) typeArgs[0];
          }
        }
      }
    }

    // Tiếp tục lên class cha cao hơn
    Class<?> parentSuperClass = superClass.getSuperclass();
    if (parentSuperClass != null && parentSuperClass != Object.class) {
      return findEntityClassInSuperclass(parentSuperClass, targetInterface);
    }

    return null;
  }

  /** Điền map với các ánh xạ từ TypeVariable đến concrete Type. */
  default void populateTypeVariableMap(Class<?> clazz, Map<TypeVariable<?>, Type> typeVariableMap) {
    // Xử lý các interface
    for (Type genericInterface : clazz.getGenericInterfaces()) {
      if (genericInterface instanceof ParameterizedType) {
        ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
        Class<?> interfaceClass = (Class<?>) parameterizedType.getRawType();
        TypeVariable<?>[] typeParameters = interfaceClass.getTypeParameters();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

        for (int i = 0; i < typeParameters.length; i++) {
          if (i < actualTypeArguments.length) {
            typeVariableMap.put(typeParameters[i], actualTypeArguments[i]);
          }
        }

        // Xử lý các interface con
        populateTypeVariableMapFromInterfaces(interfaceClass, typeVariableMap);
      }
    }

    // Xử lý class cha
    Type genericSuperclass = clazz.getGenericSuperclass();
    if (genericSuperclass instanceof ParameterizedType) {
      ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
      Class<?> superClass = (Class<?>) parameterizedType.getRawType();
      TypeVariable<?>[] typeParameters = superClass.getTypeParameters();
      Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

      for (int i = 0; i < typeParameters.length; i++) {
        if (i < actualTypeArguments.length) {
          typeVariableMap.put(typeParameters[i], actualTypeArguments[i]);
        }
      }

      // Xử lý đệ quy lên class cha
      populateTypeVariableMap(superClass, typeVariableMap);
    }
  }

  /** Điền map với các ánh xạ TypeVariable từ các interface. */
  default void populateTypeVariableMapFromInterfaces(
      Class<?> interfaceClass, Map<TypeVariable<?>, Type> typeVariableMap) {
    for (Type genericInterface : interfaceClass.getGenericInterfaces()) {
      if (genericInterface instanceof ParameterizedType) {
        ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
        Class<?> parentInterface = (Class<?>) parameterizedType.getRawType();
        TypeVariable<?>[] typeParameters = parentInterface.getTypeParameters();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

        for (int i = 0; i < typeParameters.length; i++) {
          if (i < actualTypeArguments.length) {
            typeVariableMap.put(typeParameters[i], actualTypeArguments[i]);
          }
        }

        // Xử lý đệ quy các interface cha
        populateTypeVariableMapFromInterfaces(parentInterface, typeVariableMap);
      }
    }
  }

  /**
   * Tìm interface đích chứa generic type E. Hàm này cần được tùy chỉnh theo cấu trúc interface cụ
   * thể của bạn.
   */
  default Class<?> findTargetInterface(Class<?> implementationClass) {
    // Danh sách các interface tiềm năng để kiểm tra
    // Bạn cần thay đổi điều kiện này để phù hợp với interface của bạn

    for (Class<?> iface : getAllInterfaces(implementationClass)) {
      // Có thể chỉ định trực tiếp interface đích
      // Thay thế dòng này bằng interface thực tế của bạn
      if (iface.getSimpleName().contains("Service")
          || iface.getSimpleName().contains("Repository")
          || iface.getSimpleName().contains("Dao")) {

        // Kiểm tra xem interface có ít nhất một type parameter không
        if (iface.getTypeParameters().length > 0) {
          System.out.println("Đã tìm thấy target interface: " + iface.getName());
          return iface;
        }
      }
    }

    return null;
  }

  /** Lấy tất cả các interface của một class, bao gồm cả interface kế thừa. */
  default Class<?>[] getAllInterfaces(Class<?> cls) {
    if (cls == null) {
      return new Class[0];
    }

    // Tập hợp tất cả interface kế thừa
    java.util.Set<Class<?>> interfacesFound = new java.util.LinkedHashSet<>();
    getAllInterfaces(cls, interfacesFound);

    return interfacesFound.toArray(new Class<?>[0]);
  }

  /** Lấy tất cả các interface của một class, theo cách đệ quy. */
  default void getAllInterfaces(Class<?> cls, java.util.Set<Class<?>> interfacesFound) {
    if (cls == null) {
      return;
    }

    Class<?>[] interfaces = cls.getInterfaces();
    for (Class<?> i : interfaces) {
      if (interfacesFound.add(i)) {
        getAllInterfaces(i, interfacesFound);
      }
    }

    getAllInterfaces(cls.getSuperclass(), interfacesFound);
  }
}
