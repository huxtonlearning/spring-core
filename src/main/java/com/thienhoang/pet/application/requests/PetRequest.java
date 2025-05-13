package com.thienhoang.pet.application.requests;

import com.thienhoang.pet.domain.models.enums.PetType;
import lombok.Data;

@Data
public class PetRequest {

  private String name;
  private PetType type;
  private String description;
}
