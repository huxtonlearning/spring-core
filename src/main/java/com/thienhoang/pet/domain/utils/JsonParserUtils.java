package com.thienhoang.pet.domain.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

public class JsonParserUtils {

  public static Map<String, String> toMap(String mapString) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      Map<String, String> map =
          objectMapper.readValue(mapString, new TypeReference<Map<String, String>>() {});
      return map;
    } catch (Exception e) {
      return new HashMap<>();
    }
  }

  public static <T> T entity(String json, Class<T> tClass) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();

      return objectMapper.readValue(json, tClass);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
