package com.thienhoang.pet.domain.models.entities;

import com.thienhoang.common.models.entities.AuditingEntity;
import com.thienhoang.pet.domain.models.enums.PetType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Pet extends AuditingEntity {
  private String name;
  private PetType type;
  private String description;
}
