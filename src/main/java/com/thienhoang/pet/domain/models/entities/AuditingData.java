package com.thienhoang.pet.domain.models.entities;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
@Setter
@Getter
public class AuditingData {
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
}
