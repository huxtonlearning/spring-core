package com.thienhoang.pet.domain.models.values.requests;

import com.thienhoang.pet.domain.models.enums.PetType;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class PetRequest {

  @Column(nullable = false, length = 255)
  private String name;

  private PetType type;
  private String description;
}
