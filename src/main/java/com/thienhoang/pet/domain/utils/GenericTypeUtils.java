package com.thienhoang.pet.domain.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public class GenericTypeUtils {

  /**
   * Tạo instance mới của entity class thông qua reflection. Phương thức này sử dụng cơ chế
   * SuperTypeTokenHolder để giải quyết vấn đề Type Erasure.
   *
   * @return Instance mới của entity class
   * @throws RuntimeException nếu không thể tạo instance mới
   */
  @SuppressWarnings("unchecked")
  public static <T, S> T getNewInstance(S superClass) {
    try {
      // Lấy class thông qua helper cải tiến
      Class<T> entityClass = getEntityClassFromGeneric(superClass);

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
  public static <T, S> Class<T> getEntityClassFromGeneric(S superClass) {
    Class<?> implementationClass = superClass.getClass();

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
      return (Class<T>) actualType;
    } else if (actualType instanceof ParameterizedType) {
      Type rawType = ((ParameterizedType) actualType).getRawType();
      if (rawType instanceof Class) {
        return (Class<T>) rawType;
      }
    }

    // Một phương pháp fallback khác
    return findEntityClassFallback(implementationClass, targetInterface);
  }

  /**
   * Phương pháp dự phòng để tìm entity class. Quét trực tiếp thông qua các interface đã triển khai.
   */
  @SuppressWarnings("unchecked")
  static <T> Class<T> findEntityClassFallback(
      Class<?> implementationClass, Class<?> targetInterface) {
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
            return (Class<T>) typeArgs[0];
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
  static <T> Class<T> findEntityClassInSuperclass(Class<?> superClass, Class<?> targetInterface) {
    // Kiểm tra các interface của superclass
    for (Type genericInterface : superClass.getGenericInterfaces()) {
      if (genericInterface instanceof ParameterizedType) {
        ParameterizedType paramType = (ParameterizedType) genericInterface;

        if (paramType.getRawType().equals(targetInterface)) {
          Type[] typeArgs = paramType.getActualTypeArguments();
          if (typeArgs.length > 0 && typeArgs[0] instanceof Class) {
            System.out.println(
                "Đã tìm thấy entity class trong superclass: " + ((Class<?>) typeArgs[0]).getName());
            return (Class<T>) typeArgs[0];
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
  static void populateTypeVariableMap(Class<?> clazz, Map<TypeVariable<?>, Type> typeVariableMap) {
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
  static void populateTypeVariableMapFromInterfaces(
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
  static Class<?> findTargetInterface(Class<?> implementationClass) {
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
  static Class<?>[] getAllInterfaces(Class<?> cls) {
    if (cls == null) {
      return new Class[0];
    }

    // Tập hợp tất cả interface kế thừa
    java.util.Set<Class<?>> interfacesFound = new java.util.LinkedHashSet<>();
    getAllInterfaces(cls, interfacesFound);

    return interfacesFound.toArray(new Class<?>[0]);
  }

  /** Lấy tất cả các interface của một class, theo cách đệ quy. */
  static void getAllInterfaces(Class<?> cls, java.util.Set<Class<?>> interfacesFound) {
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
