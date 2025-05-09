package com.thienhoang.pet.domain.models.entities;

import jakarta.persistence.*;
import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
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
