package com.thienhoang.pet.domain.specifications.models.values;

import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HeaderContext {
  private Long userId;
  private String name;
  private String username;
  private Map<String, Object> headers;
}
