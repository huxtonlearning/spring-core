package com.thienhoang.pet.domain.models.entities;

import com.thienhoang.common.models.entities.AuditingEntity;
import com.thienhoang.pet.domain.models.enums.PetType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Table(name = "pet")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Pet extends AuditingEntity {
  @Column(nullable = false)
  private String name;

  private PetType type;

  @Column(nullable = false)
  private String description;
}
