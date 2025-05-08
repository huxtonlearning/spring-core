package com.thienhoang.pet.domain.models.values.params;

import com.thienhoang.pet.domain.models.enums.PetType;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class PetParams {

  private String keyword;
  private PetType type;

  private Timestamp fromCreatedAt;
  private Timestamp toCreatedAt;
}
