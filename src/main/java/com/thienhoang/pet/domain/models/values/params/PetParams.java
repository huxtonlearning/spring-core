package com.thienhoang.pet.domain.models.values.params;

import com.thienhoang.pet.domain.models.enums.PetType;
import com.thienhoang.pet.domain.specifications.models.values.params.IParams;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class PetParams implements Serializable, IParams {

  private PetType type;

  private Timestamp fromCreatedAt;
  private Timestamp toCreatedAt;
}
