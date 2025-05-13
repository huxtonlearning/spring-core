package com.thienhoang.pet.infrastructure.persistence.repository;

import com.thienhoang.pet.infrastructure.persistence.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PetRepository
    extends JpaRepository<PetEntity, Long>, JpaSpecificationExecutor<PetEntity> {}
