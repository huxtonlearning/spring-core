package com.thienhoang.pet.domain.utils;

import java.beans.FeatureDescriptor;
import java.lang.reflect.Constructor;
import java.util.stream.Stream;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class FnCommon {

  public static void copyProperties(Object target, Object source) {
    BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    //    BeanUtils.copyProperties(source, target);
  }

  public static void copyAllProperties(Object target, Object source) {
    BeanUtils.copyProperties(source, target);
  }

  public static <T> T copyNonNullProperties(Class<T> clazz, Object source) {
    try {
      Constructor<?> targetIntance = clazz.getDeclaredConstructor();
      targetIntance.setAccessible(true);

      T target = (T) targetIntance.newInstance();
      BeanUtils.copyProperties(source, target, getNullPropertyNames(source));

      return target;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static <T> T copyProperties(Class<T> clazz, Object source) {
    try {
      Constructor<?> targetIntance = clazz.getDeclaredConstructor();
      targetIntance.setAccessible(true);

      T target = (T) targetIntance.newInstance();
      BeanUtils.copyProperties(source, target, getNullPropertyNames(source));

      return target;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String[] getNullPropertyNames(Object source) {
    BeanWrapper wrappedSource = new BeanWrapperImpl(source);
    return Stream.of(wrappedSource.getPropertyDescriptors())
        .map(FeatureDescriptor::getName)
        .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
        .toArray(String[]::new);
  }
}
