package com.thienhoang.pet.application.responses;

import com.thienhoang.pet.domain.models.enums.PetType;
import lombok.Data;

@Data
public class PetResponse {
  private String name;
  private PetType type;
  private String description;
}
