package com.thienhoang.pet.domain.models.entities;

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
public class Pet extends AuditingData {

  @Column(nullable = false)
  private String name;

  private PetType type;

  @Column(nullable = false)
  private String description;

  @Embedded private AuditingData data;
}
