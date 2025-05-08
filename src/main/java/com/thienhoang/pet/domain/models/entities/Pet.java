package com.thienhoang.pet.domain.models.entities;

import com.thienhoang.pet.domain.models.enums.PetType;
import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Table(name = "pet")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Pet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long creatorId;

  @Column(nullable = false)
  private Long modifierId;

  @CreationTimestamp private Timestamp createdAt;
  @UpdateTimestamp private Timestamp modifiedAt;
  private boolean isDeleted = false;

  @Column(nullable = false)
  private String name;

  private PetType type;

  @Column(nullable = false)
  private String description;
}
