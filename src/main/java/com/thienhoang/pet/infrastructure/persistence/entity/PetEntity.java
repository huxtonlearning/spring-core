package com.thienhoang.pet.infrastructure.persistence.entity;

import com.thienhoang.common.models.entities.AuditingEntity;
import com.thienhoang.pet.domain.models.enums.PetType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Table(name = "pet")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PetEntity extends AuditingEntity {

  @Column(nullable = false)
  private String name;

  private PetType type;

  @Column(nullable = false)
  private String description;
}
