package com.thienhoang.pet.domain.specifications.models.values.response;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessageResponse {
  private Integer status;
  private Date timestamp;
  private String message;
  private String messageCode;
  private String description;
  private String path;
}
