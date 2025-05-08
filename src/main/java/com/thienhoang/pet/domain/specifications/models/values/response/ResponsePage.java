package com.thienhoang.pet.domain.specifications.models.values.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class ResponsePage<T> implements Serializable {

  public ResponsePage() {}

  public ResponsePage(Page<T> pageData) {
    data = pageData.getContent();
    totalPages = pageData.getTotalPages();
    totalElements = pageData.getTotalElements();
    page = pageData.getPageable().getPageNumber();
    size = pageData.getPageable().getPageSize();
  }

  private List data = new ArrayList<>();

  private long totalElements;
  private int totalPages;
  private int page;
  private int size;

  private Map<String, Object> metadata = new HashMap<>();
}
